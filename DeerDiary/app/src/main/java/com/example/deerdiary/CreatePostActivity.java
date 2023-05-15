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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.Objects;


public class CreatePostActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    private TextView contentField;
    private TextView titleField;
    private ArrayList<DiaryEntry> diaryEntries;
    private FormValidation validation;

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
            validation = new FormValidation(titleField,contentField,diaryEntries);
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

    public void createNewEntry() {
        DiaryEntry newEntry = null;
        String userId, titleValue, contentValue, dateTime;
        String id = "";

        try {
            if (validation.areFieldsPopulated() && !validation.titleValidation()) {

                // retrieve current user id from MainActivity
                userId = MainActivity.currentUserInfo.getString("userId");
                titleValue = Objects.requireNonNull(titleField.getText()).toString();
                contentValue = Objects.requireNonNull(contentField.getText()).toString();

                // retrieve current system time
                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                String uuid = UUID.randomUUID().toString();
                id = uuid;
                newEntry = new DiaryEntry(userId, titleValue, contentValue, dateTime, id);
            } else {
                return;
            }
        } catch (Exception e) {
            quickMakeText("Failed to populate new diary fields: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (newEntry != null) {
                diaryRef.document(id).set(newEntry);
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                quickMakeText("Successfully created a new diary entry");
            }
        } catch (Exception e) {
            quickMakeText("Failed to create a new diary entry");
        }
    }

    public void quickMakeText(String text){
        Toast.makeText(CreatePostActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}