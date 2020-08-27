package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BtActivity extends AppCompatActivity {

    private Button btn_connect;
    private String mUser, currentDate, currentTime;
    private String hasilDeteksi = "7";


    //model
    private User user;
    private HasilTest postTest;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

        user = new User();
        postTest = new HasilTest();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_connect = findViewById(R.id.btn_bt_connect);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        getDataUserFirebase();

        //date
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //time
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        currentTime = format.format(calendar.getTime());

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahHasilTest();
                Intent intent = new Intent(BtActivity.this, HomeActivity.class);
                intent.putExtra("hasilDeteksi",hasilDeteksi);
                intent.putExtra("tanggal", currentDate);
                startActivity(intent);
                finish();
            }
        });
    }

    private void tambahHasilTest() {
        HasilTest postTest = new HasilTest(user.getId(), user.getUsername(), currentDate, currentTime, hasilDeteksi);
        db.collection("Data Test").document().set(postTest);
        Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();

    }

    private void getDataUserFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}