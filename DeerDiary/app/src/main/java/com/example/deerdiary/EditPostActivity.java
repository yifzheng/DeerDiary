package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EditPostActivity extends AppCompatActivity {

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

        dateView.setText(date.substring(0,11));
        titleView.setText(title);
        contentView.setText(content);
    }
}