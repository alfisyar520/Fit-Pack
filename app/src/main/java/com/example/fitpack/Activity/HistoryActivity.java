package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitpack.Adapter.ListPostHistoryAdapter;
import com.example.fitpack.Fragment.DatePickerFragment;
import com.example.fitpack.Model.HasilTest;
import com.example.fitpack.Model.User;
import com.example.fitpack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView tv_pickDate, tv_name;
    private ImageView iv_profile;

    public String mUser;

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
    private ListPostHistoryAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        hasilTest = new HasilTest();
        user = new User();

        recyclerView = findViewById(R.id.rv_history_hasil);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tv_pickDate = findViewById(R.id.tv_history_date);
        tv_name = findViewById(R.id.tv_history_name);
        iv_profile = findViewById(R.id.image_history_profile);
        
        tv_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker =  new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_name.setText(user.getUsername());
                Glide.with(HistoryActivity.this).load(user.getImageUrl()).into(iv_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getDataFromFirestore();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        tv_pickDate.setText(currentDateString);
        getDataFromFirestore();
    }

    private void getDataFromFirestore(){
        db.collection("Data Test").orderBy("currentTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                downModelArrayList.clear();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    hasilTest = documentSnapshot.toObject(HasilTest.class);
                    if (Objects.equals(documentSnapshot.get("userID"), mUser)){
                        if (Objects.equals(documentSnapshot.get("currentDate"), tv_pickDate.getText().toString())){
                            downModelArrayList.add(hasilTest);
                        }
                    }
                }
                myAdapter = new ListPostHistoryAdapter(HistoryActivity.this, downModelArrayList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }
}