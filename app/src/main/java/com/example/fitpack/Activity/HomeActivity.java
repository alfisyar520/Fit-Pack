package com.example.fitpack.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitpack.R;

public class HomeActivity extends AppCompatActivity {

    Button btn_obat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_obat = findViewById(R.id.btn_home_obat);

        btn_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ObatActivity.class);
                startActivity(intent);
            }
        });
    }
}