package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreatePostActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    private TextInputEditText contentField;
    private TextInputEditText titleField;
    private ArrayList<DiaryEntry> diaryEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final Button createButton = findViewById(R.id.createpost_create_button);
        final Button discardButton = findViewById(R.id.createpost_discard_button);
        contentField = findViewById(R.id.createpost_content_field);
        titleField = findViewById(R.id.createpost_title_field);

        try {
            diaryEntries = MainActivity.currentUserInfo.getParcelableArrayList("diaryEntries");
        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        createButton.setOnClickListener(v -> createNewEntry());

        discardButton.setOnClickListener(v -> startActivity(new Intent(CreatePostActivity.this, MainActivity.class)));
    }

    // function to display menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    // handle icons clicked on menu bar
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(CreatePostActivity.this, ViewUserProfile.class));
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(CreatePostActivity.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNewEntry(){
        DiaryEntry newEntry = null;
        String userId, titleValue, contentValue, dateTime;

        try {
            if (areFieldsPopulated() && !doesTitleAlreadyExist()) {

                // retrieve current user id from MainActivity
                userId = MainActivity.currentUserInfo.getString("userId");
                titleValue = Objects.requireNonNull(titleField.getText()).toString();
                contentValue = Objects.requireNonNull(contentField.getText()).toString();

                // retrieve current system time
                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                newEntry = new DiaryEntry(userId, titleValue, contentValue, dateTime);
            } else {
                return;
            }
        } catch (Exception e) {
            quickMakeText("Failed to populate new diary fields: " + e.getMessage());
            e.printStackTrace();
        }

        if (newEntry != null) {
            diaryRef.add(newEntry).addOnSuccessListener(documentReference -> {
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                quickMakeText("Successfully created a new diary entry");
            }).addOnFailureListener(e -> quickMakeText("Failed to create a new diary entry: " + e.getMessage()));
        } else {
            quickMakeText("Failed to create a new diary entry: new entry was never initialized");
        }
    }

    public boolean areFieldsPopulated() {
        if (TextUtils.isEmpty(titleField.getText())){
            titleField.setError("Title cannot be empty");
            titleField.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(contentField.getText())) {
            contentField.setError("Content cannot be empty");
            contentField.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean doesTitleAlreadyExist(){
        if (diaryEntries != null) {
            for (DiaryEntry entry : diaryEntries) {

                // look for any equivalent strings in titles of the current user's diaries
                if (entry.getTitle().equals(Objects.requireNonNull(titleField.getText()).toString())) {
                    titleField.setError("Title already exists");
                    titleField.requestFocus();
                    return true;
                }
            }
        }

        return false;
    }

    public void quickMakeText(String text){
        Toast.makeText(CreatePostActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}