package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitpack.Adapter.ListPostDaftarPasienAdapter;
import com.example.fitpack.Adapter.ListPostHistoryAdapter;
import com.example.fitpack.Model.HasilTest;
import com.example.fitpack.Model.User;
import com.example.fitpack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeDokterActivity extends AppCompatActivity {

    private TextView tv_namaDokter;
    private ImageView iv_profil;

    private SwipeRefreshLayout refreshLayout;

    public String mUser;
    private List<User> mUsers;

    //model
    private User user;
    private HasilTest hasilTest;

    //firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference reference;
    private FirebaseStorage storage;

    ArrayList<HasilTest> downModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListPostDaftarPasienAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dokter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        user = new User();
        mUsers = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_homeDokter_hasil);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.topBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tv_namaDokter = findViewById(R.id.tv_homeDokter_namaDokter);
        iv_profil = findViewById(R.id.image_homeDokter_profile);
        refreshLayout = findViewById(R.id.swapRefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        myAdapter.notifyDataSetChanged();
                        getDataFromFirestore();
                    }
                }, 1000);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_namaDokter.setText(user.getUsername());
                Glide.with(HomeDokterActivity.this).load(user.getImageUrl()).into(iv_profil);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getDataFromFirestore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //getMenuInflater().inflate(R.menu.menu, menu);

        /*
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search1);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDataFromFirestoreSearch(s);

                return true;
            }
        });

         */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeDokterActivity.this, LoginActivity.class));
                return true;
            case R.id.menu_search1:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setBackgroundColor(Color.WHITE);
                searchView.setQueryHint("Search Here!");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        getDataFromFirestoreSearch(s);

                        return true;
                    }
                });
            case R.id.menu_refresh:
                getDataFromFirestore();
                return true;
        }
        return false;
    }

    private void getDataFromFirestore() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);

                    if (!user.getId().equals(fUser.getUid())){
                        if (!user.getKategori().equals("dokter")){
                            mUsers.add(user);
                        }
                    }
                }

                myAdapter = new ListPostDaftarPasienAdapter(HomeDokterActivity.this, mUsers);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromFirestoreSearch(final String key) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);

                    if (!user.getId().equals(fUser.getUid())){
                        if (!user.getKategori().equals("dokter")){
                            if (user.getUsername().equals(key)){
                                mUsers.add(user);
                            }
                        }
                    }
                }

                myAdapter = new ListPostDaftarPasienAdapter(HomeDokterActivity.this, mUsers);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}