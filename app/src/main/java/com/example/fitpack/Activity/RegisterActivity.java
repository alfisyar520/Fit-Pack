package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitpack.Fragment.DatePickerFragment;
import com.example.fitpack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText et_name, et_email, et_password, et_rePassword;
    private Button btn_signUp;
    private TextView tv_signIn, tv_tanggal_lahir;
    private LinearLayout ll_tanggalLahir;
    private String kategori;

    //firebase
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.input_register_name);
        et_email = findViewById(R.id.input_register_email);
        tv_tanggal_lahir = findViewById(R.id.tv_register_tanggal);
        ll_tanggalLahir = findViewById(R.id.ll_register_pilih_tanggal);
        et_password = findViewById(R.id.input_register_password);
        et_rePassword = findViewById(R.id.input_register_repassword);
        btn_signUp = findViewById(R.id.btn_register_signUp);
        tv_signIn = findViewById(R.id.tv_register_login);

        getIncomingIntent();

        ll_tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker =  new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        auth = FirebaseAuth.getInstance();

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = et_name.getText().toString();
                String txt_email = et_email.getText().toString();
                String txt_tanggal = tv_tanggal_lahir.getText().toString();
                String txt_password = et_password.getText().toString();
                String txt_repassword = et_rePassword.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_tanggal)){
                    Toast.makeText(RegisterActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else if(!txt_password.equals(txt_repassword)){
                    Toast.makeText(RegisterActivity.this, "password and re-password is not match", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                    //pd.dismiss();
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        tv_tanggal_lahir.setText(currentDateString);
    }

    private void getIncomingIntent() {
        kategori = getIntent().getStringExtra("kategori");
    }

    private void register(final String username, String email, final String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", username);
                            hashMap.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/handycrafts-shop.appspot.com/o/default.png?alt=media&token=a721ee3d-b599-40fc-aab7-f58cadc15861");
                            hashMap.put("kategori", kategori);
                            hashMap.put("tanggallahir", tv_tanggal_lahir.getText().toString());


                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        if (kategori.equals("personal")) {
                                            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Toast.makeText(RegisterActivity.this, "Register Anda Berhasil!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(RegisterActivity.this, HomeDokterActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Toast.makeText(RegisterActivity.this, "Register Anda Berhasil!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterActivity.this, "Register Anda Gagal!", Toast.LENGTH_SHORT).show();
                            //pd.dismiss();
                        }
                    }
                });
    }
}