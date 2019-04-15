package com.example.park_n_go;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    // Field variables
    private EditText emailTV, passwordTV;
    private Button loginBtn;
    private Button regBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        initializeUIComponents();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
        registerNewUser();
    }

    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);

        final String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            if(email.equals("admin@parkgo.com")){
                                Intent intent = new Intent(SignInActivity.this, AdminDashboardActivity.class);
                                startActivity(intent);
                            }

                            else{

                            // Starting another activity
                            Intent intent = new Intent(SignInActivity.this, MapsHomeActivity.class);
                            startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void initializeUIComponents() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);
        regBtn=findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
    }

    private void registerNewUser(){
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
              startActivity(intent);
            }
        });
    }
}
