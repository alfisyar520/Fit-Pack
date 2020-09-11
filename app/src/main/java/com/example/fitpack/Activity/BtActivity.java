package com.example.fitpack.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class BtActivity extends AppCompatActivity {

    private Button btn_ambil;
    private TextView tv_koneksi, tv_coba, judul;
    private String mUser, currentDate, currentTime, idBt;
    private String hasilDeteksi;

    private String coba_hasil;

    //model
    private User user;
    private HasilTest postTest;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    //bt
    //String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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

        getIncomingIntent();

        btn_ambil = findViewById(R.id.btn_bt_ambil);
        tv_koneksi = findViewById(R.id.tv_bt_1);
        tv_coba = findViewById(R.id.tv_bt_2);
        judul = findViewById(R.id.tv_bt_name);

        judul.setText("BLUETOOTH");

        new ConnectBT().execute(); //Call the class to connect
        tv_koneksi.setText("Connected\n"+idBt);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        getDataUserFirebase();

        //date
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //time
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        currentTime = format.format(calendar.getTime());

        btn_ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataAll();
                //coba_hasil = tv_coba.getText().toString();
                Toast.makeText(BtActivity.this, hasilDeteksi, Toast.LENGTH_SHORT).show();

                tambahHasilTest();
                Intent intent = new Intent(BtActivity.this, HomeActivity.class);
                intent.putExtra("hasilDeteksi",hasilDeteksi);
                intent.putExtra("tanggal", currentDate);
                Disconnect();
                startActivity(intent);
                finish();
            }
        });
    }

    public void getIncomingIntent(){
        idBt = getIntent().getStringExtra("EXTRA_ADDRESS");
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

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(BtActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(idBt);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public void getDataAll(){
        try {
            InputStream inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());

            try {
                for (int i = 0; i < 1 ; i++){
                    char baru = (char) inputStream.read();
                    //kumpulan.add(String.valueOf(baru));
                    //tv_coba.append(String.valueOf(baru));
                    hasilDeteksi = String.valueOf(baru);
                    //tv3.append("\n");
                }
            }catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                //tv_coba.setText(String.valueOf(e));
            }


        } catch (IOException e) {
            msg(e.toString());
            //e.printStackTrace();
        }
    }
}