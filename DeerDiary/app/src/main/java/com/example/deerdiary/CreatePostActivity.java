package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ArrayList<JSONObject> diaryEntries = new ArrayList<JSONObject>();

        final Button createButton = findViewById(R.id.createpost_create_button);
        final Button discardButton = findViewById(R.id.createpost_discard_button);
        final TextInputLayout content = findViewById(R.id.createpost_content);
        final TextInputLayout title = findViewById(R.id.createpost_title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject newEntry = new JSONObject();
                try {
                    newEntry.put("title", title.getEditText().getText());
                    newEntry.put("content",title.getEditText().getText());
                    newEntry.put("dateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
                    diaryEntries.add(newEntry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(CreatePostActivity.this, StartActivity.class));
                Toast.makeText(CreatePostActivity.this, newEntry.toString(), Toast.LENGTH_SHORT).show();
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