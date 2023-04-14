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
    private final CollectionReference diaryRef = db.collection("diaryEntries");
    public TextInputLayout content;
    public TextInputLayout title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final Button createButton = findViewById(R.id.createpost_create_button);
        final Button discardButton = findViewById(R.id.createpost_discard_button);
        content = findViewById(R.id.createpost_content);
        title = findViewById(R.id.createpost_title);

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
        Map<String, Object> newEntry = new HashMap<>();
        String userId = MainActivity.currentUserInfo.getString("userId");

        try {
            if (areFieldsPopulated()) {

                // retrieving current user id from MainActivity
                newEntry.put("userId", userId);
                newEntry.put("title", title.getEditText().getText().toString());
                newEntry.put("content", content.getEditText().getText().toString());

                // retrieve current system time
                newEntry.put("dateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));

            } else {
                return;
            }
        } catch (Exception e) {
            quickMakeText("Failed to populate new diary fields: " + e.getMessage());
            e.printStackTrace();
        }

        diaryRef.document(userId).set(newEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class));
                quickMakeText("Successfully created a new diary");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                quickMakeText("Failed to create a new diary: " + e.getMessage());
            }
        });
    }

    public boolean areFieldsPopulated() {
        if (TextUtils.isEmpty(title.getEditText().getText())){
            title.setError("Title cannot be empty");
            title.requestFocus();
            quickMakeText("Title cannot be empty");
            return false;
        } else if (TextUtils.isEmpty(content.getEditText().getText())) {
            content.setError("Content cannot be empty");
            content.requestFocus();
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