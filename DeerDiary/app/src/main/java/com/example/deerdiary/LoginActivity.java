package com.example.deerdiary;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deerdiary.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;

    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View v = activityLoginBinding.getRoot();
        setContentView(v);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // set input type of password edit field
        activityLoginBinding.loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        activityLoginBinding.loginButton.setOnClickListener(view -> loginUser());
        activityLoginBinding.loginRegisterhere.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    public void loginUser(){
        String email = Objects.requireNonNull(activityLoginBinding.loginEmail.getText()).toString();
        String password = Objects.requireNonNull(activityLoginBinding.loginPassword.getText()).toString();

        if (TextUtils.isEmpty(email)) {
            activityLoginBinding.loginEmail.setError("Email cannot be empty");
            activityLoginBinding.loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            activityLoginBinding.loginPassword.setError("Password cannot be empty");
            activityLoginBinding.loginPassword.requestFocus();
        } else {
            // sign in with email and password
            // if successful
            myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = myAuth.getCurrentUser(); // get current user object
                    assert user != null;
                    if (user.isEmailVerified()) {
                        // check if email is verified and if not they will not be routed to main page
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}