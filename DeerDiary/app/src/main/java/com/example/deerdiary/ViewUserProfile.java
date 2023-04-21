package com.example.deerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewUserProfile extends AppCompatActivity {
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FOLDER_NAME = "images";

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imageRef = storage.getReference().child(FOLDER_NAME);
    private ActivityViewUserProfileBinding activityViewUserProfileBinding;
    private ArrayList<DiaryEntry> diaryEntries;
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

    @Override
    protected void onStart(){
        super.onStart();
        String userId = MainActivity.currentUserInfo.getString("userId");
        diaryEntries = MainActivity.currentUserInfo.getParcelableArrayList("diaryEntries");
        int size = diaryEntries.size();
        userRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        User user = document.toObject(User.class);
                        activityViewUserProfileBinding.userProfileName.setText(user.getLastName() + ", " + user.getFirstName());
                        activityViewUserProfileBinding.userProfileEmail.setText(user.getEmail());
                        if (diaryEntries != null)
                        {
                            activityViewUserProfileBinding.userProfileEntriesCount.setText(Integer.toString(size));
                        }
                        else
                        {
                            activityViewUserProfileBinding.userProfileEntriesCount.setText("0");
                        }
                        if (user.getImageURL() != null)
                        {
                            imageRef.child(user.getImageURL() + FILE_EXTENSION).getDownloadUrl().addOnSuccessListener(uri -> {
                               Glide.with(ViewUserProfile.this).load(uri).into(activityViewUserProfileBinding.userProfileImg);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(ViewUserProfile.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });;
                        }
                        else
                        {
                            activityViewUserProfileBinding.userProfileImg.setImageResource(R.mipmap.ic_profile);
                        }
                    }
                }
            }
        });
    }
}