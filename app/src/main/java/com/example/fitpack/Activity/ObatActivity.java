package com.example.fitpack.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.fitpack.HerbalFragment;
import com.example.fitpack.NonHerbalFragment;
import com.example.fitpack.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ObatActivity extends AppCompatActivity {

    private static final String TAG = ObatActivity.class.getSimpleName();
    ChipNavigationBar navigationBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat);

        navigationBar = findViewById(R.id.nv_obat_navigationBar);



        if (savedInstanceState == null){
            navigationBar.setItemSelected(R.id.non_herbal, true);
            fragmentManager = getSupportFragmentManager();
            NonHerbalFragment nonHerbalFragment = new NonHerbalFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_obat_fragmentContainer, nonHerbalFragment)
                    .commit();
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.herbal:
                        fragment = new HerbalFragment();
                        break;
                    case R.id.non_herbal:
                        fragment = new NonHerbalFragment();
                        break;
                }

                if (fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fl_obat_fragmentContainer, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }


}