package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final Button createButton = findViewById(R.id.createpost_create_button);
        final Button discardButton = findViewById(R.id.createpost_discard_button);
        final TextInputLayout content = findViewById(R.id.createpost_content);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class));
                Toast.makeText(CreatePostActivity.this, content.getEditText().getText(), Toast.LENGTH_SHORT).show();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class));
            }
        });

    }
}