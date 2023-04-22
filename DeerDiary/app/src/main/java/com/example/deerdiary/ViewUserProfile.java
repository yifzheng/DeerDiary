package com.example.deerdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.deerdiary.databinding.ActivityViewUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewUserProfile extends AppCompatActivity {
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FOLDER_NAME = "images";
    private static final String USER_UID = "userUID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String IMAGE_URI = "imageURI";

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imageRef = storage.getReference().child(FOLDER_NAME);
    private ActivityViewUserProfileBinding activityViewUserProfileBinding;
    private User user;

    private static Bundle userObj = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewUserProfileBinding = ActivityViewUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityViewUserProfileBinding.getRoot());

        activityViewUserProfileBinding.userProfileEditBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ViewUserProfile.this, EditUserProfile.class);
            intent.putExtra(USER_UID, myAuth.getCurrentUser().getUid());
            intent.putExtra(FIRST_NAME, user.getFirstName());
            intent.putExtra(LAST_NAME, user.getLastName());
            intent.putExtra(IMAGE_URI, user.getImageURL());
            startActivity(intent);
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        String userId = currentUser.getUid();
        int size = MainActivity.currentUserInfo.getInt("entryCount");

        userRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(User.class);
                        activityViewUserProfileBinding.userProfileName.setText(user.getLastName() + ", " + user.getFirstName());
                        activityViewUserProfileBinding.userProfileEmail.setText(user.getEmail());
                        activityViewUserProfileBinding.userProfileEntriesCount.setText(Integer.toString(size));
                        if (user.getImageURL() != null) {
                            imageRef.child(user.getImageURL() + FILE_EXTENSION).getDownloadUrl().addOnSuccessListener(uri -> {
                                Glide.with(ViewUserProfile.this).load(uri).into(activityViewUserProfileBinding.userProfileImg);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(ViewUserProfile.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                            ;
                        } else {
                            activityViewUserProfileBinding.userProfileImg.setImageResource(R.mipmap.ic_profile);
                        }
                    }
                }
            }
        });
    }
}