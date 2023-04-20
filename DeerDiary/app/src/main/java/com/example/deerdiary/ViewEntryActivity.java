package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewEntryActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_detail);

        String date = getIntent().getStringExtra("DATE");
        String title = getIntent().getStringExtra("TITLE");
        String content = getIntent().getStringExtra("CONTENT");

        TextView dateView = findViewById(R.id.datetimeView);
        TextView titleView = findViewById(R.id.titleView);
        TextView contentView = findViewById(R.id.contentView);

        dateView.setText(date.substring(0, 11));
        titleView.setText(title);
        contentView.setText(content);

        Button returnBtn = (Button) findViewById(R.id.return_btn);
        returnBtn.setOnClickListener(view -> {
            startActivity(new Intent(ViewEntryActivity.this, MainActivity.class));
        });
    }
}