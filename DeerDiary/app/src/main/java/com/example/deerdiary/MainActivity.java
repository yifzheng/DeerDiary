package com.example.deerdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecycleViewInterface, AdapterView.OnItemSelectedListener {

    private final FirebaseAuth myAuth = FirebaseAuth.getInstance();
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

        setupSort();
        recyclerView =findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize((true));

        entries= new ArrayList<>();
        adapter = new Adapter(MainActivity.this, entries, this);

        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart(){
        super.onStart();

        currentUser = myAuth.getCurrentUser();
        assert currentUser != null;
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
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(MainActivity.this, ViewUserProfile.class));
                return true;
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

    @SuppressLint("NotifyDataSetChanged")
    public void retrieveDiaryData(){
        // Retrieve all the diaries where the userId of DiaryEntry objects in diaryRef matches the current userId
        Query query = diaryRef.whereEqualTo("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        query.get().addOnCompleteListener(task -> {
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
                currentUserInfo.putInt("entryCount", entries.size());
            }

        });

    }
    public void setupSort(){
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_type, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    //handle click on the entries list
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ViewEntryActivity.class);

        intent.putExtra("DATE",entries.get(position).dateTime);
        intent.putExtra("TITLE",entries.get(position).title);
        intent.putExtra("CONTENT",entries.get(position).content);
        intent.putExtra("ID",entries.get(position).id);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
