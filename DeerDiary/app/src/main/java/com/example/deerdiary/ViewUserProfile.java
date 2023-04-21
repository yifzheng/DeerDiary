package com.example.deerdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.deerdiary.databinding.ActivityViewUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ViewUserProfile extends AppCompatActivity {

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private ActivityViewUserProfileBinding activityViewUserProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewUserProfileBinding = ActivityViewUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityViewUserProfileBinding.getRoot());

        activityViewUserProfileBinding.userProfileEditBtn.setOnClickListener(view -> Toast.makeText(this, "edit user profile", Toast.LENGTH_SHORT).show());
        activityViewUserProfileBinding.userProfileHomeBtn.setOnClickListener(view -> {
            startActivity(new Intent(ViewUserProfile.this, MainActivity.class));
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
            case R.id.menu_profile:
                Toast.makeText(this, "clicking user profile icon", Toast.LENGTH_SHORT).show();
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(ViewUserProfile.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(ViewUserProfile.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}