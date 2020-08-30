package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    private ImageView iv_profile;
    private TextView tv_name, tv_tanggal, tv_hasil_indikasi;
    private TextView tv_bening, tv_coklat, tv_putihSusu, tv_putihKeju, tv_hijau, tv_kuning, tv_abu;
    private ImageView iv_bau, iv_tidakBau, iv_warna_kosong, iv_bau_kosong;
    private String hasilDeteksi, namaPasienDariDokter;
    private Button btn_obat, btn_saran, btn_history;

    private String mUser;

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();

        btn_obat = findViewById(R.id.btn_home_obat);
        iv_profile = findViewById(R.id.image_home_profile);
        tv_name = findViewById(R.id.tv_home_name);
        tv_tanggal = findViewById(R.id.tv_home_tanggal);
        btn_saran = findViewById(R.id.btn_home_saran);
        btn_history = findViewById(R.id.btn_home_history);

        //not empty
        tv_bening = findViewById(R.id.tv_home_warna_bening);
        tv_coklat = findViewById(R.id.tv_home_warna_coklat);
        tv_putihSusu = findViewById(R.id.tv_home_warna_putih_susu);
        tv_putihKeju = findViewById(R.id.tv_home_warna_putih_keju);
        tv_hijau = findViewById(R.id.tv_home_warna_hijau);
        tv_kuning = findViewById(R.id.tv_home_warna_putih_kuning);
        tv_abu = findViewById(R.id.tv_home_warna_putih_abu);
        iv_bau = findViewById(R.id.iv_home_bau);
        iv_tidakBau = findViewById(R.id.iv_home_tidakBau);
        tv_hasil_indikasi = findViewById(R.id.tv_home_hasilIndikasi);

        //empty
        iv_warna_kosong = findViewById(R.id.iv_home_warnaKosong);
        iv_bau_kosong = findViewById(R.id.iv_home_bauKosong);

        Toolbar toolbar = findViewById(R.id.topBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getIncomingIntent();
        if (hasilDeteksi != null){
            inisialisasiWarnaBauIndikasi();
        } else {
            inisialisasiDefault();
        }

        btn_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasilDeteksi != null){
                    Intent intent = new Intent(HomeActivity.this, ObatActivity.class);
                    intent.putExtra("hasilDeteksi", hasilDeteksi);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Silahkan cek kesehatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_saran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasilDeteksi != null){
                    Intent intent = new Intent(HomeActivity.this, SaranActivity.class);
                    intent.putExtra("hasilDeteksi", hasilDeteksi);
                    intent.putExtra("tanggal", tv_tanggal.getText());
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Silahkan cek kesehatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (namaPasienDariDokter != null){
            btn_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.super.onBackPressed();
                }
            });
        } else {
            btn_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (namaPasienDariDokter == null){
                    tv_name.setText(user.getUsername());
                } else {
                    tv_name.setText(namaPasienDariDokter);
                }
                Glide.with(HomeActivity.this).load(user.getImageUrl()).into(iv_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
        }
        return false;
    }

    public void getIncomingIntent(){
        hasilDeteksi = getIntent().getStringExtra("hasilDeteksi");
        tv_tanggal.setText(getIntent().getStringExtra("tanggal"));
        namaPasienDariDokter = getIntent().getStringExtra("namaPasien");
    }

    private void inisialisasiDefault() {
        tv_bening.setVisibility(View.GONE);
        tv_coklat.setVisibility(View.GONE);
        tv_putihSusu.setVisibility(View.GONE);
        tv_putihKeju.setVisibility(View.GONE);
        tv_hijau.setVisibility(View.GONE);
        tv_kuning.setVisibility(View.GONE);
        tv_abu.setVisibility(View.GONE);
        iv_bau.setVisibility(View.GONE);
        iv_tidakBau.setVisibility(View.GONE);
        tv_hasil_indikasi.setText("-");
        tv_tanggal.setText("-");
    }

    public void inisialisasiWarnaBauIndikasi(){
        if (hasilDeteksi.equals("1")){
            tv_coklat.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_bau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Normal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else if (hasilDeteksi.equals("2")){
            tv_bening.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_bau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Normal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else if (hasilDeteksi.equals("3")){
            tv_bening.setVisibility(View.GONE);
            tv_coklat.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_bau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Normal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else if (hasilDeteksi.equals("4")){
            tv_bening.setVisibility(View.GONE);
            tv_coklat.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_bau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Abnormal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else if (hasilDeteksi.equals("5")){
            tv_bening.setVisibility(View.GONE);
            tv_coklat.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_tidakBau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Abnormal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else if (hasilDeteksi.equals("6")){
            tv_bening.setVisibility(View.GONE);
            tv_coklat.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_abu.setVisibility(View.GONE);
            iv_tidakBau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Abnormal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        } else {
            tv_bening.setVisibility(View.GONE);
            tv_coklat.setVisibility(View.GONE);
            tv_putihSusu.setVisibility(View.GONE);
            tv_putihKeju.setVisibility(View.GONE);
            tv_hijau.setVisibility(View.GONE);
            tv_kuning.setVisibility(View.GONE);
            iv_tidakBau.setVisibility(View.GONE);
            tv_hasil_indikasi.setText("Abnormal");
            iv_bau_kosong.setVisibility(View.GONE);
            iv_warna_kosong.setVisibility(View.GONE);
        }
    }
}