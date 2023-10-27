package com.example.appnew.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.appnew.DocTinTucActivity;
import com.example.appnew.R;
import com.example.appnew.adapter.HienThiTinTucAdminAdapter;
import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.enity.TinTuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DaDuyetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DaDuyetFragment extends Fragment implements ItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DaDuyetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DaDuyetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DaDuyetFragment newInstance(String param1, String param2) {
        DaDuyetFragment fragment = new DaDuyetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private View view;
    HienThiTinTucAdminAdapter adapter;
    RecyclerView recyclerView;
    List<TinTuc> lstTinTucs;
    private FirebaseFirestore firestore;
    private MaterialButton btnTinTucAdd;
    BottomNavigationView bottomNavigationView;

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
        view = inflater.inflate(R.layout.fragment_da_duyet, container, false);
        recyclerView = view.findViewById(R.id.rvTinTucAdmin_dd);
        lstTinTucs = new ArrayList<>();
        adapter = new HienThiTinTucAdminAdapter(lstTinTucs ,getActivity(), this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        getDataFromTinTuc();

        return view;
    }
    private void getDataFromTinTuc() {
        firestore.collection("TinTuc")
                .orderBy("NgayDang", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot querySnapshot = task.getResult();

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                TinTuc tinTuc = new TinTuc();
                                tinTuc.setTenBaiBao(document.getString("TenBaiBao"));
                                tinTuc.setNoiDung(document.getString("NoiDung"));
                                tinTuc.setIDBaiBao(document.getId());
                                tinTuc.setAnh(document.getString("AnhBia"));
                                tinTuc.setTrangThai(document.getString("TrangThai"));

                                Timestamp timestamp = document.getTimestamp("NgayDang");
                                if (timestamp != null) {
                                    // Lấy giá trị thời gian từ Timestamp
                                    long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;

                                    // Tạo đối tượng Date từ milliseconds
                                    Date date = new Date(milliseconds);

                                    // Sử dụng đối tượng Date cho các mục đích khác nhau trong mã của bạn
                                    // Ví dụ:
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                    String formattedTimestamp = dateFormat.format(date);

                                    tinTuc.setNgayDang(formattedTimestamp);

                                }

                                if (tinTuc.getTrangThai().equals("Đã duyệt")) {
                                    lstTinTucs.add(tinTuc);
                                }
                            }
                            // Cập nhật RecyclerView
                            adapter.notifyDataSetChanged();
                        } else {
                            // Xử lý khi có lỗi xảy ra
                        }
                    }
                });
    }
    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(getActivity(), DocTinTucActivity.class);
        String[] fixInput = id.split("IDBaiBao:");
        intent.putExtra("IDTinTuc", fixInput[1]);
        startActivity(intent);
    }
}