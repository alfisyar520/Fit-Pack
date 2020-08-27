package com.example.fitpack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitpack.Activity.HistoryActivity;
import com.example.fitpack.Model.HasilTest;
import com.example.fitpack.Model.User;
import com.example.fitpack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListPostDaftarPasienAdapter extends RecyclerView.Adapter<ListPostDaftarPasienAdapter.ListViewHolder>{

    //private Map<String, String> percobaan = new Hashtable<>();

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String mUser;
    private Context mContext;
    private Map<String, HasilTest> data;
    private List<User> listPasien;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_pasien, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        return new ListViewHolder(view);
    }

    public ListPostDaftarPasienAdapter(Context mContext, List<User> listPasien){
        this.mContext = mContext;
        this.listPasien = listPasien;
    }

    @Override
    public void onBindViewHolder(final ListPostDaftarPasienAdapter.ListViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();

        holder.tv_nama.setText(listPasien.get(position).getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = listPasien.get(holder.getAdapterPosition()).getId();
                String nama = listPasien.get(holder.getAdapterPosition()).getUsername();

                Intent intent = new Intent(mContext, HistoryActivity.class);
                intent.putExtra("userId", userID);
                intent.putExtra("namaPasien", nama);
                mContext.startActivity(intent);

                /*
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                })

                 */
            }
        });

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfPic = listMakanan.get(holder.getAdapterPosition()).getImage();
                CollectionReference docRefDataPost = db.collection("Data Postingan");
                docRefDataPost.whereEqualTo("image", nameOfPic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                MakananModel makananModel = documentSnapshot.toObject(MakananModel.class);
                                Intent hasil_start = new Intent(mContext, HasilActivity.class);
                                hasil_start.putExtra("id_makanan", documentSnapshot.getId());
                                hasil_start.putExtra("namaMakanan", makananModel.getTopMakanan());
                                hasil_start.putExtra("userID", makananModel.getUserID());
                                hasil_start.putExtra("image", makananModel.getImage());
                                hasil_start.putExtra("usernamePublisher", makananModel.getUsernamePublisher());
                                hasil_start.putExtra("currentDate", makananModel.getCurrentDate());
                                hasil_start.putExtra("currentTime", makananModel.getCurrentTime());
                                hasil_start.putExtra("topMakanan", makananModel.getTopMakanan());

                                mContext.startActivity(hasil_start);
                            }

                        }
                    }
                });
            }
        });

         */
    }


    @Override
    public int getItemCount() {
        return listPasien.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.icv_pasien_nama);
        }
    }
}
