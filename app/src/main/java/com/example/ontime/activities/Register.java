package com.example.ontime.activities;

import static com.example.ontime.view.CustomToast.showError;
import static com.example.ontime.view.CustomToast.showSuccess;
import static com.example.ontime.view.CustomToast.showWarning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.example.ontime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private TextView loginnow;
    private TextInputLayout tlPassword;
    private Button btnRegister;

    ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etUsername = findViewById(R.id.username);
        etEmail = findViewById(R.id.email);
        tlPassword = findViewById(R.id.password_layout);
        etPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        loginnow = findViewById(R.id.Loginnow);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tlPassword.setEndIconVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();
                int red = ContextCompat.getColor(Register.this, R.color.red);
                int green = ContextCompat.getColor(Register.this, R.color.gren);
                if(passwordInput.length() == 0){
                    tlPassword.setHelperText("At least 8 characters must contain uppercase, lowercase, and numbers.");
                    tlPassword.setHelperTextColor(ColorStateList.valueOf(green));
                }else if (passwordInput.length() >= 8) {
                    if (hasLowerCase(passwordInput) && hasUpperCase(passwordInput) && hasNumber(passwordInput)) {
                        tlPassword.setHelperText("Correct\n");
                        tlPassword.setHelperTextColor(ColorStateList.valueOf(green));
                    } else {
                        tlPassword.setHelperText("Must include uppercase, lowercase, and numbers");
                        tlPassword.setHelperTextColor(ColorStateList.valueOf(red));
                    }
                } else {
                    tlPassword.setHelperText("At least 8 characters\n");
                    tlPassword.setHelperTextColor(ColorStateList.valueOf(red));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                // validation
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {

                    if(username.isEmpty()){
                        showError(etUsername,"Data must be filled");
                    }

                    if(email.isEmpty()){
                        showError(etEmail,"Data must be filled");
                    }

                    if(password.isEmpty()){
                        showError(etPassword,"Data must be filled");
                        tlPassword.setEndIconVisible(false);
                    }

                    return;
                } else if (!(email.contains("@") && email.contains("."))) {
                    showError(etEmail,"Incorrect email format");
                    return;
                } else if (!(password.length() >= 8 && hasLowerCase(password) && hasUpperCase(password) && hasNumber(password))) {
                    showWarning(getApplicationContext(),"Incorect password format");
                    return;
                }

                progressBar.bringToFront();
                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        showSuccess(getApplicationContext(),"Register Success. Please verify your email to login.");
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    } else {
                                      showWarning(getApplicationContext(),"Verification Failed");
                                    }
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            showWarning(getApplicationContext(),task.getException().getMessage());
                        }
                    }
                });
            }
        });


        loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    public boolean hasUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLowerCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}