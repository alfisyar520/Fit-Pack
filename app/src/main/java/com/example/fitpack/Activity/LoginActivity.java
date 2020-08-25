package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitpack.Model.User;
import com.example.fitpack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private TextView tv_signUp, tv_forgetPassword;
    private Button btn_login;

    //firebase
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login_login);
        tv_signUp = findViewById(R.id.tv_login_signUp);
        tv_forgetPassword = findViewById(R.id.tv_login_forgot);

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PilihKategoriActivity.class);
                startActivity(intent);
            }
        });


        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();
        /*
        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getKategori().equals("personal")){
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(LoginActivity.this, HomeDokterActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

         */

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = et_email.getText().toString();
                String txt_password = et_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                if (user.getKategori().equals("personal")){
                                                    Intent toDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                                                    toDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(toDashboard);
                                                    finish();
                                                    //pd.dismiss();
                                                }else{
                                                    Intent toDashboard = new Intent(LoginActivity.this, HomeDokterActivity.class);
                                                    toDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(toDashboard);
                                                    finish();
                                                    //pd.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentification failed!", Toast.LENGTH_SHORT).show();
                                        //pd.dismiss();
                                    }
                                }
                            });
                }
            }
        });


    }
}