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
 * Use the {@link NonHerbalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonHerbalFragment extends Fragment {

    private TextView tv_nonHerbal1, tv_nonHerbal2, tv_nonHerbal3, tv_nonHerbal4, tv_nonHerbal5, tv_info;
    private Dialog mDialog;
    private String hasilDeteksi;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NonHerbalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NonHerbalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NonHerbalFragment newInstance(String param1, String param2) {
        NonHerbalFragment fragment = new NonHerbalFragment();
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
        getIncomingIntent();

        View view = inflater.inflate(R.layout.fragment_non_herbal, container, false);
        inisialisasi(view);

        if (hasilDeteksi.equals("A") || hasilDeteksi.equals("B") || hasilDeteksi.equals("C")){
            tv_nonHerbal1.setText("Anda tidak memerlukan obat untuk masalah keputihan normal anda");
            tv_nonHerbal2.setVisibility(view.GONE);
            tv_nonHerbal3.setVisibility(view.GONE);
            tv_nonHerbal4.setVisibility(view.GONE);
            tv_nonHerbal5.setVisibility(view.GONE);
        } else if (hasilDeteksi.equals("D")){
            tv_nonHerbal1.setText("Fluconazole 150-200 mg sehari selama tiga hari. Setelah itu mengkonsumsi fluconazole 200 mg selama 1 minggu sekali untuk 2 bulan, dilanjutkan 200 mg fluconazole untuk 2 minggu sekali selama 4 bulan, dilanjutkan 200 mg fluconazole selama satu bulan sekali untuk 6 bulan.");
            tv_nonHerbal2.setText("Itraconazole 20 mg 2 kali sehari untuk 1 hari");
            tv_nonHerbal3.setText("Untuk pemakaian luar dapat menggunakan :\n" +
                    "-\tClotrimazole cream 1% atau 2% diaplikasikan 2-3 kali sehari\n" +
                    "-\tKetoconazole cream 2% diaplikasikan 1-2 kali sehari");
            tv_nonHerbal4.setVisibility(view.GONE);
            tv_nonHerbal5.setVisibility(view.GONE);
        } else if (hasilDeteksi.equals("E") || hasilDeteksi.equals("F")){
            tv_nonHerbal1.setText("Obat untuk dosis standart");
            tv_nonHerbal1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Metronidazole 400-500 mg sehari dua kali dikonsumsi selama 5-7 hari");
                    mDialog.show();
                }
            });

            tv_nonHerbal2.setText("Obat untuk dosis tinggi");
            tv_nonHerbal2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Metronidazole atau tiniazole 2 g sehari dikonsumsi selama 5-7 hari\n\n" +
                            "Metronidazole 800 mg dikonsumsi 3 kali sehari selama 7 hari");
                    mDialog.show();
                }
            });

            tv_nonHerbal3.setText("Obat untuk dosis sangat tinggi");
            tv_nonHerbal3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_info.setText("Tinidazole 1 g dikonsumsi 2-3 kali sehari selama 14 hari");
                    mDialog.show();
                }
            });

            tv_nonHerbal4.setText("Catatan : \n" +
                    "Hindari penggunaan atau meminum alkohol setelah mengkonsumsi atapun menggunakan metronidazole setidaknya 48, dan untuk pengguna tinadazole setidaknya 72 jam.");
            tv_nonHerbal5.setVisibility(view.GONE);
        } else {
            tv_nonHerbal1.setText("Metronidazole 400 mg oral tablet dikonsumsi sehari dua kali selama 5-7 hari");
            tv_nonHerbal2.setText("Metronidazole 2 g oral dosis tunggal (hanya dikonsumsi satu kali saja)");
            tv_nonHerbal3.setText("Jika tidak toleransi dengan pengkonsumsian obat, alternative yang bisa digunakan yaitu :\n" +
                    "-\tMetronidazole gel (0,75%) dioleskan pada vagina sehari sekali selama 5 hari\n" +
                    "-\tClindamycin cream (2%) (5 g) dioleskan pada vagina sehari sekali selama 7 hari.");
            tv_nonHerbal4.setText("Alternatif obat lain :\n" +
                    "-\tTinidazole 2g dosis tunggal, hanya dikonsumsi satu kali saja.\n" +
                    "-\tClindamycin 300 mg dikonsumsi sehari dua kali selama 7 hari");
            tv_nonHerbal5.setText("Catatan :\n" +
                    "-\tHindari penggunaan atau meminum alkohol setelah mengkonsumsi atapun menggunakan metronidazole\n" +
                    "-\tHindar berhubungan sexual selama 5 hari setelah mengkonsumsi ataupun menggunakan clindamycin.");
        }

        return view;
    }

    private void getIncomingIntent(){
        hasilDeteksi = getActivity().getIntent().getStringExtra("hasilDeteksi");
    }

    private void inisialisasi(View view) {
        tv_nonHerbal1 = view.findViewById(R.id.tv_nonHerbal_1);
        tv_nonHerbal2 = view.findViewById(R.id.tv_nonHerbal_2);
        tv_nonHerbal3 = view.findViewById(R.id.tv_nonHerbal_3);
        tv_nonHerbal4 = view.findViewById(R.id.tv_nonHerbal_4);
        tv_nonHerbal5 = view.findViewById(R.id.tv_nonHerbal_5);

        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_info = (TextView) mDialog.findViewById(R.id.tv_info);
    }
}