package com.example.appnew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.appnews.R;
import com.example.appnews.adapter.DanhMucAdapter;
import com.example.appnews.adapter.ItemCallback;
import com.example.appnews.enity.DanhMuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DanhMuc24hFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhMuc24hFragment extends Fragment implements ItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DanhMuc24hFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DanhMuc24hFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DanhMuc24hFragment newInstance(String param1, String param2) {
        DanhMuc24hFragment fragment = new DanhMuc24hFragment();
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
        return inflater.inflate(R.layout.fragment_danh_muc24h, container, false);
    }

    RecyclerView rvDanhMuc;
    DanhMucAdapter adapter;
    List<DanhMuc> lstDanhMuc;
    TrangChuFragment trangChuFragment;
    private FirebaseFirestore firestore;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        assert activity != null;
        rvDanhMuc = activity.findViewById(R.id.rv_danhMuc24h);
        lstDanhMuc = new ArrayList<>();
        adapter = new DanhMucAdapter(lstDanhMuc ,getActivity(), this);
        rvDanhMuc.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvDanhMuc.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();
        // Lấy dữ liệu từ Firestore và cập nhật RecyclerView
        getDataFromFirestore();
    }
    private void getDataFromFirestore() {
        firestore.collection("DanhMuc")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                DanhMuc danhMuc = new DanhMuc();
                                danhMuc.setTenDanhMuc(document.getString("TenDanhMuc"));
                                danhMuc.setAnh(document.getString("Anh"));
                                danhMuc.setLink(document.getString("Link"));
                                danhMuc.setIDDanhMuc(document.getId());
                                if (danhMuc.getLink().equals("")){
                                    lstDanhMuc.add(danhMuc);
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
    DanhMuc danhMuc;
    @Override
    public void onItemClick(String id) {
        danhMuc = new DanhMuc();
        trangChuFragment = new TrangChuFragment();
        String[] fixInput = id.split("IDDanhMuc :");

        firestore.collection("DanhMuc").document(String.valueOf(fixInput[1]))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Lấy thông tin từ tài liệu
                            danhMuc.setTenDanhMuc(document.getString("TenDanhMuc"));
                            danhMuc.setAnh(document.getString("Anh"));
                            danhMuc.setLink(document.getString("Link"));
                            Bundle bundle = new Bundle();
                            bundle.putString("key", danhMuc.getTenDanhMuc());
                            trangChuFragment.setArguments(bundle);
                            switchFragment(trangChuFragment);
                        }
                    }
                });

    }
    private void switchFragment(Fragment trangChuFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = ((FragmentManager) fragmentManager).beginTransaction();
        ((FragmentTransaction) fragmentTransaction).replace(R.id.main_fragment, trangChuFragment); // Thay thế Fragment hiện tại bằng Fragment mới
        fragmentTransaction.addToBackStack(null); // Thêm Fragment hiện tại vào Back Stack để có thể quay lại sau này
        fragmentTransaction.commit(); // Xác nhận giao dịch Fragment
    }
}