package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class SaranActivity extends AppCompatActivity {

    private TextView tv_name, tv_tanggal, tv_umum1, tv_umum2, tv_umum3, tv_khusus1, tv_khusus2;
    private ImageView iv_profile;
    private String mUser;
    private String hasilDeteksi = "3";

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();

        tv_name = findViewById(R.id.tv_saran_name);
        tv_tanggal = findViewById(R.id.tv_saran_tanggal);
        iv_profile = findViewById(R.id.image_saran_profile);

        Toolbar toolbar = findViewById(R.id.topBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_name.setText(user.getUsername());
                Glide.with(SaranActivity.this).load(user.getImageUrl()).into(iv_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setUmum();
        setKhusus();

    }

    private void setKhusus() {
        tv_khusus1 = findViewById(R.id.tv_saran_khusus_1);
        tv_khusus2 = findViewById(R.id.tv_saran_khusus_2);

        if (hasilDeteksi.equals("1") || hasilDeteksi.equals("2") || hasilDeteksi.equals("3")){
            tv_khusus1.setText("Tidak ada penanganan khusus. Tetap jaga kebersihan organ kewanitaan dan usahakan untuk tetap kering. Hal-hal yang bisa dilakukan :");
            tv_khusus2.setText("-\tJangan menggunakan pembersih daerah kewanitaan yang memiliki kandungan alkohol dan parfume\n" +
                    "-\tGunakan celana dalam yang tidak ketat dan dapat menyerap keringat\n" +
                    "-\tMembersihkan daerah kewanitaan dari arah depan ke belakang bukan sebaliknya\n" +
                    "-\tJangan gunakan pantyliner atapun tisu toilet yang mengandung parfume\n" +
                    "-\tHindari terlalu sering dengan jangka waktu yang lama ketika berendam dengan air panas\n" +
                    "-\tMakan makanan yang bergizi serta konsumsi yogurt ataupun suplemen yang mengandung lactobacillus");
        } else if (hasilDeteksi.equals("4")){
            tv_khusus1.setText("Hal yang harus dilakukan :");
            tv_khusus2.setText("-\tHindari menggunakan celana dalam yang ketat \n" +
                    "-\tPilih bahan celana dalam yang dapat menyerap keringat\n" +
                    "-\tGunakan pelembab vagina untuk yang memiliki kulit kering kemudian dibilas, pelembab ini sebagai pengganti sabun.");
        } else if (hasilDeteksi.equals("5") || hasilDeteksi.equals("6")){
            tv_khusus1.setText("Hal yang harus dilakukan :");
            tv_khusus2.setText("Hindari untuk berhubungan sexual dengan pasangan hingga pengobatan selesai atau hingga dinyatakan sembuh");
        } else {
            tv_khusus1.setText("Hal yang harus dilakukan :");
            tv_khusus2.setText("-\tHindari pembersihan area vagina menggunakan anti septik, sabun mandi.");
        }

    }

    private void setUmum() {
        tv_umum1 = findViewById(R.id.tv_saran_umum_1);
        tv_umum2 = findViewById(R.id.tv_saran_umum_2);
        tv_umum3 = findViewById(R.id.tv_saran_umum_3);

        tv_umum1.setText("-\tHindari makanan yang panas, biarkan dingin terlebih dahulu beru dikonsumsi\n" +
                "-\tHindari minum teh dan kopi\n" +
                "-\tHindari mengkonsumsi daging, nasi, jamur, pisang yang dicampur dengan susu, dan mengkonsumsi kelapa sebelum tidur\n" +
                "-\tMelakukan diet rendah gula");

        tv_umum2.setText("-\tGunakan celana dalam dengan bahan yang mudah menyerap keringat\n" +
                "-\tHindari penggunaan celana dalam yang ketat.");

        tv_umum3.setText("-\tGunakan garam mandi ataupun air garam untuk membersihkan daerah kewanitaan\n" +
                "-\tGunakan campuran jambu biji dan pembersih higienis\n" +
                "-\tGunakan rebusan daun sirih atau bubuk mawar untuk membersihkan daerah kewanitaan\n" +
                "-\tHindari berendam dengan air panas yang terlalu lama\n" +
                "-\tHindari penggunaan sabun dengan kandungan parfume atau busa mandi\n" +
                "-\tJangan terlalu sering membasuh daerah kewanitaan");
    }
}