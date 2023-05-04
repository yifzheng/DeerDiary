package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.deerdiary.databinding.ActivityViewUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ViewUserProfile extends AppCompatActivity {
    private static final String FOLDER_NAME = "images";
    private static final String USER_UID = "userUID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String IMAGE_URI = "imageURI";

    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = db.collection("User");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference imageRef = storage.getReference().child(FOLDER_NAME);
    private ActivityViewUserProfileBinding activityViewUserProfileBinding;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewUserProfileBinding = ActivityViewUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityViewUserProfileBinding.getRoot());

        activityViewUserProfileBinding.userProfileEditBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ViewUserProfile.this, EditUserProfile.class);
            intent.putExtra(USER_UID, Objects.requireNonNull(myAuth.getCurrentUser()).getUid());
            intent.putExtra(FIRST_NAME, user.getFirstName());
            intent.putExtra(LAST_NAME, user.getLastName());
            intent.putExtra(IMAGE_URI, user.getImageURL());
            startActivity(intent);
        });
        activityViewUserProfileBinding.userProfileHomeBtn.setOnClickListener(view -> startActivity(new Intent(ViewUserProfile.this, MainActivity.class)));
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
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(ViewUserProfile.this, MainActivity.class));
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(ViewUserProfile.this, ViewUserProfile.class));
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        assert currentUser != null;
        String userId = currentUser.getUid();
        int size = MainActivity.currentUserInfo.getInt("entryCount");

        userRef.document(userId).addSnapshotListener(this, (value, error) -> {
            if (error != null)
            {
                Log.d("VIEW_USER_PROFILE", error.toString());
            }
            else
            {
                assert value != null;
                if (value.exists())
                {
                    user = value.toObject(User.class);
                    assert user != null;
                    activityViewUserProfileBinding.userProfileName.setText(user.getFirstName() + " " + user.getLastName());
                    activityViewUserProfileBinding.userProfileEmail.setText(user.getEmail());
                    activityViewUserProfileBinding.userProfileEntriesCount.setText(Integer.toString(size));
                    if (user.getImageURL() != null) {
                        imageRef.child(user.getImageURL()).getDownloadUrl().addOnSuccessListener(uri ->
                                Glide.with(ViewUserProfile.this).load(uri).into(activityViewUserProfileBinding.userProfileImg)).addOnFailureListener(e ->
                                Toast.makeText(ViewUserProfile.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        activityViewUserProfileBinding.userProfileImg.setImageResource(R.mipmap.ic_profile);
                    }
                }
            }
        });
    }
}