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
    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    private FormValidation validation;
    private ArrayList<DiaryEntry> diaryEntries;
    private TextView dateField;
    private TextView titleField;
    private TextView contentField;
    private String date,title,content,id;
    private boolean isClicked;// Checks if the edit button is clicked
    private Button return_delete_Btn;
    private Button editBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_detail);

        dateField = findViewById(R.id.datetimeView);
        titleField = findViewById(R.id.titleView);
        contentField = findViewById(R.id.contentView);

        //unable to edit the title and content field
        setEnable(false);

        //display diary entry data
        showEntryData();

        //Return button will change to delete button after the edit button is clicked
        return_delete_Btn = (Button) findViewById(R.id.return_delete_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);

        try {
            diaryEntries = MainActivity.currentUserInfo.getParcelableArrayList("diaryEntries");
            validation = new FormValidation(titleField,contentField,diaryEntries);
        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        editBtn.setOnClickListener(view -> {
            if(isClicked){
                editEntry();
            }
            else {
                return_delete_Btn.setText("DELETE");
                editBtn.setText("SAVE");
                setEnable(true);
            }
        });

        return_delete_Btn.setOnClickListener(view -> {
            if(isClicked){
                deleteEntry();
            }
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
                startActivity(new Intent(ViewEntryActivity.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showEntryData(){
        date = getIntent().getStringExtra("DATE");
        title = getIntent().getStringExtra("TITLE");
        content = getIntent().getStringExtra("CONTENT");
        id = getIntent().getStringExtra("ID");

        dateField.setText(date.substring(0, 11));
        titleField.setText(title);
        contentField.setText(content);
    }

    public void editEntry(){
        try{
            if(title.equals(titleField.getText().toString())){
                return_delete_Btn.setText("RETURN");
                editBtn.setText("EDIT");
                setEnable(false);
            }
            else if(validation.areFieldsPopulated() && !validation.titleValidation()){
                if(isTitleChanged() | isContentChanged()) {
                    Toast.makeText(ViewEntryActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                }
                return_delete_Btn.setText("RETURN");
                editBtn.setText("EDIT");
                setEnable(false);
            }

        }
        catch (Exception e) {
            String text = "Failed to edit existing diary fields: "+e.getMessage();
            Toast.makeText(ViewEntryActivity.this,text, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void deleteEntry(){
        diaryRef.document(id).delete();
    }
    public boolean isTitleChanged(){
        if(!title.equals(titleField.getText().toString())) {
            diaryRef.document(id).update("title", titleField.getText().toString());
            return true;
        }
        else
            return false;
    }
    public boolean isContentChanged(){
        if(!content.equals(contentField.getText().toString())){
            diaryRef.document(id).update("content",contentField.getText().toString());
            return true;
        }
        else
            return false;
    }
    //If the edit button is click, the fields are enable else no
    public void setEnable(boolean status){
        isClicked = status;
        titleField.setFocusableInTouchMode(status);
        titleField.setFocusable(status);
        contentField.setFocusableInTouchMode(status);
        contentField.setFocusable(status);
    }
}