package com.example.deerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEntry();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
            }
        });
    }

    public void createNewEntry(){
        DiaryEntry newEntry = null;
        String userId, titleValue, contentValue, dateTime;

        try {
            if (areFieldsPopulated() && !doesTitleAlreadyExist()) {

                // retrieve current user id from MainActivity
                userId = MainActivity.currentUserInfo.getString("userId");
                titleValue = titleField.getText().toString();
                contentValue = contentField.getText().toString();

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
            diaryRef.add(newEntry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                    quickMakeText("Successfully created a new diary entry");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    quickMakeText("Failed to create a new diary entry: " + e.getMessage());
                }
            });
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
                if (entry.getTitle().equals(titleField.getText().toString())) {
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