package com.example.leafingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.leafingapp.adapter.SearchUserRecycler;
import com.example.leafingapp.model.UserModel;
import com.example.leafingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUser extends AppCompatActivity {
    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    SearchUserRecycler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);


        searchInput = findViewById(R.id.seach_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus(); //kada udjemo u pogled fokusira se na search i otvori nam tekst

        backButton.setOnClickListener(v -> goBack());
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Invalid Username");
                return;
                //kada udjemo u pogled fokusira se na search i otvori nam tekst
            }
            setupSearchRecyclerView(searchTerm);
        });

    }
    private void goBack() {

        onBackPressed();
    }

    void setupSearchRecyclerView(String searchTerm){

        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecycler(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
/*Ukratko, adapter je most između podataka iz Firestore baze podataka i RecyclerView-a, omogućavajući RecyclerView-u
 da dinamički prikazuje podatke i reaguje na promene u tim podacima.Adapter se koristi kako bi RecyclerView mogao prikazati podatke iz Firestore baze podataka.
  Bez adaptera, RecyclerView ne bi znao kako prikazati podatke i kako reagovati na promene u tim podacima. Ovaj adapter specifično prilagođen
  je za prikazivanje podataka tipa UserModel i omogućava RecyclerView-u da prikaže listu korisnika koji zadovoljavaju određeni kriterijum pretrage.*/
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}