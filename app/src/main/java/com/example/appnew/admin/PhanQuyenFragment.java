package com.example.appnew.admin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.adapter.PhanQuyenAdapter;
import com.example.appnew.enity.TaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhanQuyenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhanQuyenFragment extends Fragment implements ItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhanQuyenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhanQuyenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhanQuyenFragment newInstance(String param1, String param2) {
        PhanQuyenFragment fragment = new PhanQuyenFragment();
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

    List<TaiKhoan> lstsTaiKhoan;
    PhanQuyenAdapter adapter;
    RecyclerView rvTaiKhoan;
    Dialog dialog;
    EditText edtEmail;
    Button btnCapNhap, btnHuy;
    Spinner spnQuyen;
    TaiKhoan taiKhoan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phan_quyen, container, false);

        rvTaiKhoan = view.findViewById(R.id.rvPhanQuyenAdmin);
        lstsTaiKhoan = new ArrayList<>();
        dialog = new Dialog(getActivity());

        adapter = new PhanQuyenAdapter(lstsTaiKhoan, getActivity(), this);
        rvTaiKhoan.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        rvTaiKhoan.setAdapter(adapter);

        getDataTaiKhoan();
        return view;
    }
    private void opendialog(String id) {
        dialog.setContentView(R.layout.custom_dialog_user_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCapNhap = dialog.findViewById(R.id.btn_capNhap_phanquyen);
        btnHuy = dialog.findViewById(R.id.btn_close_PhanQuyen);
        edtEmail = dialog.findViewById(R.id.ed_email_phanquyen);
        spnQuyen = dialog.findViewById(R.id.phanQuyenSpinner);

        taiKhoan = new TaiKhoan();

        FirebaseFirestore.getInstance().collection("users").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                taiKhoan.setQuyen(document.getString("VaiTro"));
                                taiKhoan.setIdTaiKhoan(document.getId());
                                edtEmail.setText(document.getString("Email"));
                                String vaiTro = document.getString("VaiTro");
                                List<String> lstTenQuyen = new ArrayList<>();
                                FirebaseFirestore.getInstance().collection("QuyenHan")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String name = document.getString("TenQuyen");
                                                        lstTenQuyen.add(name);
                                                    }
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lstTenQuyen);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spnQuyen.setAdapter(adapter);
                                                    if (vaiTro != null) {
                                                        int index = lstTenQuyen.indexOf(vaiTro);
                                                        if (index != -1) {
                                                            spnQuyen.setSelection(index);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedQuyen = spnQuyen.getSelectedItem().toString();
                Map<String, Object> map = new HashMap<>();
                map.put("VaiTro", selectedQuyen);
                FirebaseFirestore.getInstance().collection("users").document(id)
                        .update(map)
                        .addOnSuccessListener(aVoid -> {
                            // Thực hiện thành công
                            Toast.makeText(getActivity(), "Cập nhật quyền hạn thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi
                            Toast.makeText(getActivity(), "Lỗi khi cập nhật quyền hạn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
        // --------------------------------------------------------------------------
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // --------------------------------------------------------------------------
        dialog.show();
    }
    private void getDataTaiKhoan() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                TaiKhoan taiKhoan = new TaiKhoan();
                                taiKhoan.setIdTaiKhoan(document.getId());
                                taiKhoan.setEmail(document.getString("Email"));
                                taiKhoan.setQuyen(document.getString("VaiTro"));
                                lstsTaiKhoan.add(taiKhoan);
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
        String[] fixInput = id.split("IDTK :");
        opendialog(fixInput[1]);
    }
}