package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.deerdiary.databinding.ActivityStartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding activityStartBinding;

    private final FirebaseAuth myAuth = FirebaseAuth.getInstance(); // getting an instance of the auth
    private FirebaseUser currentUser; // stores current user logged into the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStartBinding = ActivityStartBinding.inflate(getLayoutInflater()); // inflate layout
        View v = activityStartBinding.getRoot(); // get root of inflated layout
        setContentView(v); // set the view of activity to layout file
        Objects.requireNonNull(getSupportActionBar()).hide(); // hides the top support bar
        // set onclick listener to login button
        activityStartBinding.startLoginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        // set onclick listener to register button
        activityStartBinding.startRegisterBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        // check for set current user and if no user logged in stay on activity else route to MainActivity
        currentUser = myAuth.getCurrentUser();
        if (currentUser != null)
        {
            startActivity(new Intent(StartActivity.this, MainActivity.class)); // move to main activity
        }
    }
}