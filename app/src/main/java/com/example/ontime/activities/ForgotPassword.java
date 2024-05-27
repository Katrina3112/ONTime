package com.example.ontime.activities;

import static com.example.ontime.view.CustomToast.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ontime.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassword extends AppCompatActivity {

    TextView backLogin;
    Button btnReset;
    EditText etEmail;
    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backLogin = findViewById(R.id.backLogin);
        btnReset = findViewById(R.id.btnReset);
        progressBar = findViewById(R.id.progressBar);
        etEmail = findViewById(R.id.email);

        auth = FirebaseAuth.getInstance();

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();

                if(!email.isEmpty()){
                    ResetPasword(email);
                }else{
                    showError(etEmail,"Email must be field");
                }
            }
        });

    }
    private  void ResetPasword(String email){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showSuccess(getApplicationContext(),"Reset Password link has been sent");
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                showWarning(getApplicationContext(),e.getMessage());
            }
        });
    }
    public void showError(TextView s,String error){

        s.setError(error);
        s.requestFocus();
    }
}