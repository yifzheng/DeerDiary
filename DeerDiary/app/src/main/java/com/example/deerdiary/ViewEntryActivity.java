package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewEntryActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    private FormValidation validation;
    private ArrayList<DiaryEntry> diaryEntries;
    private TextView dateField;
    private TextView titleField;
    private TextView contentField;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_detail);


        String date = getIntent().getStringExtra("DATE");
        String title = getIntent().getStringExtra("TITLE");
        String content = getIntent().getStringExtra("CONTENT");

        dateField = findViewById(R.id.datetimeView);
        titleField = findViewById(R.id.titleView);
        contentField = findViewById(R.id.contentView);

        dateField.setText(date.substring(0, 11));
        titleField.setText(title);
        contentField.setText(content);

        final Button returnBtn = (Button) findViewById(R.id.return_btn);
        final Button editBtn = (Button) findViewById(R.id.edit_btn);

        try {
            diaryEntries = MainActivity.currentUserInfo.getParcelableArrayList("diaryEntries");
            validation = new FormValidation(titleField,contentField,diaryEntries);
        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        editBtn.setOnClickListener(view -> {
            editEntry();
        });

        returnBtn.setOnClickListener(view -> {
            finish();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                startActivity(new Intent(ViewEntryActivity.this, ViewUserProfile.class));
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(ViewEntryActivity.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(ViewEntryActivity.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void editEntry(){
        try{
            finish();
        }
        catch (Exception e) {
            String text = "Failed to edit existing diary fields: "+e.getMessage();
            Toast.makeText(ViewEntryActivity.this,text, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
    }
    }
}