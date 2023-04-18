package com.example.deerdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference diaryRef = db.collection("diaryEntry");
    public static Bundle currentUserInfo = new Bundle(); // relevant user data to be easily accessed in other activities

    private RecyclerView recyclerView;
    private ArrayList<DiaryEntry> entries;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize((true));

        entries= new ArrayList<DiaryEntry>();
        adapter = new Adapter(MainActivity.this, entries);

        recyclerView.setAdapter(adapter);
        EvenChangeListener();
    }
    private void EvenChangeListener() {
        //Retrieve All the documents where the userId field matches the current userId
        db.collection("diaryEntry").whereEqualTo("userId",currentUserInfo.getString("userId"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error !=null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc:value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                entries.add(dc.getDocument().toObject(DiaryEntry.class));
                            }

                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();

        currentUser = myAuth.getCurrentUser();
        currentUserInfo.putString("userId", currentUser.getUid());
        retrieveDiaryData();
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
                Toast.makeText(this, "clicking profile icon", Toast.LENGTH_SHORT).show();
                return true; // have not created profile activity yet
            case R.id.menu_logout:
                myAuth.signOut(); // sign out
                startActivity(new Intent(MainActivity.this, StartActivity.class)); // move to start page
                return true;
            case R.id.menu_create_post:
                startActivity(new Intent(MainActivity.this, CreatePostActivity.class)); //move to create post page
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveDiaryData(){

        // Retrieve all the diaries where the userId of DiaryEntry objects in diaryRef matches the current userId
        diaryRef.whereEqualTo("userId", currentUserInfo.getString("userId")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<DiaryEntry> diaryEntries = new ArrayList<>();

                // Loop through all the retrieved documents
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                {
                    // Convert the retrieved document back to a DiaryEntry object
                    DiaryEntry diaryEntry = documentSnapshot.toObject(DiaryEntry.class);
                    diaryEntry.setId(documentSnapshot.getId()); // set the id of the current diary entry so we have access when we want to edit or delete it
                    diaryEntries.add(diaryEntry);
                }

                // Stored in a bundle as a parcel for easy access in other activities
                currentUserInfo.putParcelableArrayList("diaryEntries", diaryEntries);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to retrieve diaries: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
