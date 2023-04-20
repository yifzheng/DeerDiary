package com.example.deerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecycleViewInterface{

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
        adapter = new Adapter(MainActivity.this, entries, this);

        recyclerView.setAdapter(adapter);
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
        Query query = diaryRef.whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    entries.clear();
                    // Loop through all the retrieved documents
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        DiaryEntry diaryEntry = doc.toObject(DiaryEntry.class);
                        diaryEntry.setId(doc.getId());
                        entries.add(diaryEntry);
                    }
                    //Notify any registered observers that the data set has changed.
                    adapter.notifyDataSetChanged();
                    // Stored in a bundle as a parcel for easy access in other activities
                    currentUserInfo.putParcelableArrayList("diaryEntries", entries);

                }

            }
        });

    }
    //handle click on the entries list
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ViewEntryActivity.class);
        //for now I pass them as individual, later will pass it as objects

        intent.putExtra("DATE",entries.get(position).dateTime);
        intent.putExtra("TITLE",entries.get(position).title);
        intent.putExtra("CONTENT",entries.get(position).content);

        startActivity(intent);
    }
}
