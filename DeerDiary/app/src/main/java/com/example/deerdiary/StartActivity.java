package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.deerdiary.databinding.ActivityStartBinding;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding activityStartBinding;

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
        });
        // set onclick listener to register button
        activityStartBinding.startRegisterBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
        });
    }
}