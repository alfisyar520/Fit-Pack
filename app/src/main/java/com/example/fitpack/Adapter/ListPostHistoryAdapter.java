package com.example.fitpack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitpack.Activity.HomeActivity;
import com.example.fitpack.Model.HasilTest;
import com.example.fitpack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPostHistoryAdapter extends RecyclerView.Adapter<ListPostHistoryAdapter.ListViewHolder>{

    //private Map<String, String> percobaan = new Hashtable<>();

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String mUser;
    private Context mContext;
    private Map<String, HasilTest> data;
    private ArrayList<HasilTest> listHasil;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_history, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        return new ListViewHolder(view);
    }

    public ListPostHistoryAdapter(Context mContext, ArrayList<HasilTest> listHistory){
        this.mContext = mContext;
        this.listHasil = listHistory;
    }

    @Override
    public void onBindViewHolder(final ListPostHistoryAdapter.ListViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();

        holder.current_time.setText(listHasil.get(position).getCurrentTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hisTime = listHasil.get(holder.getAdapterPosition()).getCurrentTime();
                CollectionReference docRefDataPost = db.collection("Data Test");
                docRefDataPost.whereEqualTo("currentTime", hisTime).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                HasilTest hasilTest = documentSnapshot.toObject(HasilTest.class);
                                Intent intent = new Intent(mContext, HomeActivity.class);
                                intent.putExtra("tanggal", hasilTest.getCurrentDate());
                                intent.putExtra("hasilDeteksi", hasilTest.getHasilDeteksi());
                                mContext.startActivity(intent);
                            }
                        }
                    }
                });
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
        return listHasil.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView current_time;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            current_time = itemView.findViewById(R.id.icv_history_jam);
        }
    }
}
