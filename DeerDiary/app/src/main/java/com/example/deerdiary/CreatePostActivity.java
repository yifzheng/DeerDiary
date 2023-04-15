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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    public TextInputLayout contentLayout;
    public TextInputLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final Button createButton = findViewById(R.id.createpost_create_button);
        final Button discardButton = findViewById(R.id.createpost_discard_button);
        contentLayout = findViewById(R.id.createpost_content);
        titleLayout = findViewById(R.id.createpost_title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEntry();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class));
            }
        });
    }

    public void createNewEntry(){
        DiaryEntry newEntry = null;
        String userId, titleValue, contentValue, dateTime;

        try {
            if (areFieldsPopulated()) {

                // retrieve current user id from MainActivity
                userId = MainActivity.currentUserInfo.getString("userId");
                titleValue = titleLayout.getEditText().getText().toString();
                contentValue = contentLayout.getEditText().getText().toString();

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
        if (TextUtils.isEmpty(titleLayout.getEditText().getText())){
            titleLayout.setError("Title cannot be empty");
            titleLayout.requestFocus();
            quickMakeText("Title cannot be empty");
            return false;
        } else if (TextUtils.isEmpty(contentLayout.getEditText().getText())) {
            contentLayout.setError("Content cannot be empty");
            contentLayout.requestFocus();
            quickMakeText("Content cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    public void quickMakeText(String text){
        Toast.makeText(CreatePostActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}