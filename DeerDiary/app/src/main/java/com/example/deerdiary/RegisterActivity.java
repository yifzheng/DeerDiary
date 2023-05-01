package com.example.deerdiary;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deerdiary.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // reference to database
    private final CollectionReference userRef = db.collection("User"); // a reference to the User collection
    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();

    private ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View v = activityRegisterBinding.getRoot();
        setContentView(v);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // set input type of password
        activityRegisterBinding.registerPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        activityRegisterBinding.registerButton.setOnClickListener(view -> {
            createUser();
        });

        activityRegisterBinding.registerLoginhere.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    public void createUser() {
        String firstName = activityRegisterBinding.registerFirstname.getText().toString();
        String lastName = activityRegisterBinding.registerLastname.getText().toString();
        String email = activityRegisterBinding.registerEmail.getText().toString();
        String password = activityRegisterBinding.registerPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            activityRegisterBinding.registerFirstname.setError("First Name cannot be empty");
            activityRegisterBinding.registerFirstname.requestFocus();
        } else if (TextUtils.isEmpty(lastName)) {
            activityRegisterBinding.registerLastname.setError("Last Name cannot be empty");
            activityRegisterBinding.registerLastname.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            activityRegisterBinding.registerEmail.setError("Email cannot be empty");
            activityRegisterBinding.registerEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            activityRegisterBinding.registerPassword.setError("Password cannot be empty");
            activityRegisterBinding.registerPassword.requestFocus();
        } else {
            myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // if we successfully created user with email and password, we send email verification
                        myAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // create a user object to store into the database
                        Person user = new User(firstName, lastName, email);
                        // retrieve the UID of recent created user
                        String userUID = task.getResult().getUser().getUid();
                        // add user object into database with the UID as ID of the reference
                        userRef.document(userUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Error creating user: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}