package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ViewEntryActivity extends AppCompatActivity {
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
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

    // function to display menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    // handle icons clicked on menu bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                startActivity(new Intent(ViewEntryActivity.this, MainActivity.class));
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(ViewEntryActivity.this, ViewUserProfile.class));
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(ViewEntryActivity.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(ViewEntryActivity.this, ViewEntryActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}