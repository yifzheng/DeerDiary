package com.example.deerdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deerdiary.databinding.ActivityEditUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class EditUserProfile extends AppCompatActivity {
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FOLDER_NAME = "images";
    private static final String USER_UID = "userUID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String IMAGE_URI = "imageURI";

    private ActivityEditUserProfileBinding editUserProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editUserProfileBinding = ActivityEditUserProfileBinding.inflate(getLayoutInflater());
        setContentView(editUserProfileBinding.getRoot());

        Bundle extras = getIntent().getExtras();
        String firstName = extras.getString(FIRST_NAME);
        String lastName = extras.getString(LAST_NAME);
        String userUID = extras.getString(USER_UID);
        String imageURI = extras.getString(IMAGE_URI);
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
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(EditUserProfile.this, MainActivity.class));
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(EditUserProfile.this, ViewUserProfile.class));
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(EditUserProfile.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(EditUserProfile.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}