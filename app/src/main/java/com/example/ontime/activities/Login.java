package com.example.ontime.activities;

import static com.example.ontime.view.CustomToast.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ontime.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import android.content.Intent;

public class Login extends AppCompatActivity {


    private TextView signup,forgotPassword;
    private static ProgressBar progressBar;
    private FirebaseAuth mAuth,auth;
    private EditText etEmail, etPassword;
    private Button btnLogin,glogin;
    private static final int RC_SIGN_IN = 123;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        glogin = findViewById(R.id.loginViaGoogle);
        signup =findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotPassword);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                showWarning(getApplicationContext(),"Google connection failed");
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(email.isEmpty()||password.isEmpty()){
                    showWarning(getApplicationContext(),"All data must fild");
                    return;
                }

                progressBar.bringToFront();
                progressBar.setVisibility(View.VISIBLE);

                FirebaseAuthWithEmail(email, password, auth);
            }
        });



        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginGaccount();
            }
        });
    }

    private void FirebaseAuthWithEmail(String email, String password, FirebaseAuth auth) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (auth.getCurrentUser().isEmailVerified()) {
                        showSuccess(getApplicationContext(),"Login Success");
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    } else {
                        showWarning(getApplicationContext(),"Verify Email First");
                    }
                } else {
                    showWarning(getApplicationContext(),"Email or Password Wrong");
                }
            }
        });
    }

    private void loginGaccount(){
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    showSuccess(getApplicationContext(),"Login sucess");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    showWarning(getApplicationContext(),"Authentication failed");
                }
            }
        });
    }
}