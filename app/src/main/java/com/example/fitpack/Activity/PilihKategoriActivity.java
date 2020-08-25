package com.example.fitpack.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitpack.R;

public class PilihKategoriActivity extends AppCompatActivity {

    private Button btn_personal, btn_dokter;
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kategori);

        btn_personal = findViewById(R.id.btn_katogori_personal);
        btn_dokter = findViewById(R.id.btn_katogori_dokter);
        tv_login = findViewById(R.id.tv_kategori_login);

        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PilihKategoriActivity.this, RegisterActivity.class);
                intent.putExtra("kategori", "personal");
                startActivity(intent);
            }
        });

        btn_dokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PilihKategoriActivity.this, RegisterActivity.class);
                intent.putExtra("kategori", "dokter");
                startActivity(intent);
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PilihKategoriActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}