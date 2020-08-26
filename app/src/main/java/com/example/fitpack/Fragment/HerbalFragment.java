package com.example.fitpack.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitpack.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HerbalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HerbalFragment extends Fragment {

    private TextView tv_herbal1, tv_herbal2, tv_herbal3, tv_herbal4, tv_info;
    private Dialog mDialog;
    private String hasilDeteksi="3";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HerbalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HerbalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HerbalFragment newInstance(String param1, String param2) {
        HerbalFragment fragment = new HerbalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_herbal, container, false);
        View view = inflater.inflate(R.layout.fragment_herbal, container, false);
        inisialisasi(view);

        if (hasilDeteksi.equals("1") || hasilDeteksi.equals("2") || hasilDeteksi.equals("3")){
            tv_herbal1.setText("Anda bisa menggunakan rebusan air daun sirih untuk menjaga kebersihan organ kewanitaan anda");
            tv_herbal2.setVisibility(view.GONE);
            tv_herbal3.setVisibility(view.GONE);
            tv_herbal4.setVisibility(view.GONE);
        } else {
            tv_herbal1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Bubuk jahe ini digunakan dengan mencampurkan sedikit bubuk jahe dengan clotrimazol cream 1%");
                    mDialog.show();
                }
            });

            tv_herbal2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Gunakan aloe vera (tanpa alkohol dan tanpa parfume) pada daerah kewanitaan");
                    mDialog.show();
                }
            });

            tv_herbal3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Air rebusan daun sirih dapat digunakan untuk membersihkan daerah kewanitaan");
                    mDialog.show();
                }
            });
        }

        return view;
    }

    private void inisialisasi(View view) {
        tv_herbal1 = view.findViewById(R.id.tv_herbal_1);
        tv_herbal2 = view.findViewById(R.id.tv_herbal_2);
        tv_herbal3 = view.findViewById(R.id.tv_herbal_3);
        tv_herbal4 = view.findViewById(R.id.tv_herbal_4);

        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_info = (TextView) mDialog.findViewById(R.id.tv_info);
    }


}