package com.example.deerdiary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deerdiary.databinding.ActivityEditUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditUserProfile extends AppCompatActivity {
    // fire store references
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imageRef = storage.getReference().child(FOLDER_NAME);
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FOLDER_NAME = "images";
    private static final String USER_UID = "userUID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String IMAGE_URI = "imageURL";

    private ActivityEditUserProfileBinding binding;
    private ActivityResultLauncher<Intent> getContent;
    private Uri imageUri;
    private String fileName;
    private String userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        String firstName = extras.getString(FIRST_NAME);
        String lastName = extras.getString(LAST_NAME);
        userUID = extras.getString(USER_UID);
        String imageURI = extras.getString(IMAGE_URI);
        /*imageRef.child(imageURI).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(EditUserProfile.this).load(uri).into(binding.userProfileImg);
        }).addOnFailureListener(e -> {
            Toast.makeText(EditUserProfile.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
*/
        // <---- Load information into necessary fields ------------>
        binding.editUserFirstName.setText(firstName);
        binding.editUserLastName.setText(lastName);

        // retrieve image
        binding.editUserPicText.setOnClickListener(view -> openGallery());
        binding.saveProfileBtn.setOnClickListener(view -> saveProfile());
        binding.cancelProfileBtn.setOnClickListener(view -> startActivity(new Intent(EditUserProfile.this, ViewUserProfile.class)));

        // intent response
        getContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
            {
                imageUri = result.getData().getData();
                Toast.makeText(this, "Image URI" + imageUri.toString(), Toast.LENGTH_SHORT).show();
                binding.userProfileImg.setImageURI(imageUri);
            }
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

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getContent.launch(intent);
    }

    private void uploadImage()
    {
        if (imageUri != null)
        {
            fileName = System.currentTimeMillis() + FILE_EXTENSION;
            imageRef.child(fileName).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveProfile()
    {
        if (imageUri != null)
        {
            uploadImage();
        }
        String firstName = binding.editUserFirstName.getText().toString();
        String lastName = binding.editUserLastName.getText().toString();

        Map<String, Object> user = new HashMap<>();
        if (!firstName.isEmpty())
        {
            user.put(FIRST_NAME, firstName);
        }
        else if (!lastName.isEmpty())
        {
            user.put(LAST_NAME, lastName);
        }
        else if (!fileName.isEmpty())
        {
            user.put(IMAGE_URI, fileName);
        }


        userRef.document(userUID).update(user).addOnSuccessListener(unused -> {
            SystemClock.sleep(1000);
            startActivity(new Intent(EditUserProfile.this, ViewUserProfile.class));
        }).addOnFailureListener(e -> {
            Toast.makeText(EditUserProfile.this, "Error Updating User: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        });
    }
}