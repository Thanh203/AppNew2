package com.example.appnew.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appnew.DocTinTucActivity;
import com.example.appnew.R;
import com.example.appnew.SearchActivity;
import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.enity.DanhMuc;
import com.example.appnew.enity.TinTuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrangChuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrangChuFragment extends Fragment implements ItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrangChuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrangChuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrangChuFragment newInstance(String param1, String param2) {
        TrangChuFragment fragment = new TrangChuFragment();
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
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

    ListView lv;
    public List<TinTuc> ItemLists = new ArrayList<>();
    String link ;
    //
//    ArrayList<String> lsDanhMuc;
    List<DanhMuc> lstDanhMuc;
    RecyclerView rvDanhMuc;
    com.example.appnews.adapter.GoiDanhMucAdapter adapter;
    FirebaseFirestore firestore;
    List<TinTuc> lstTinTuc;
    com.example.appnews.adapter.TinTucAdapter tinTucAdapter;
    RecyclerView rvTinTuc;
    String tenDanhMuc;
    private long luotXem;
    ImageButton btSearch;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        assert activity != null;
        rvTinTuc =activity.findViewById(R.id.rv_Tintuc);
        btSearch = activity.findViewById(R.id.bt_Search);
        firestore = FirebaseFirestore.getInstance();
        lstTinTuc = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            tenDanhMuc = bundle.getString("key");
            FirebaseFirestore.getInstance().collection("TinTuc")
                    .whereEqualTo("DanhMuc", tenDanhMuc)
                    //.orderBy("NgayDang", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                    TinTuc tinTuc = new TinTuc();
                                    tinTuc.setTenBaiBao(document.getString("TenBaiBao"));
                                    tinTuc.setNoiDung(document.getString("NoiDung"));
                                    tinTuc.setIDBaiBao(document.getId());
                                    tinTuc.setAnh(document.getString("AnhBia"));
                                    tinTuc.setTacGia(document.getString("TacGia"));
//                                  tinTuc.setLinkBaiBao(document.get("LinkTinTuc").toString());
                                    tinTuc.setIDDanhMuc(document.getString("DanhMuc"));
                                    tinTuc.setSoLuotXem(Integer.parseInt(document.get("LuotXem").toString()));
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
                                    if(tinTuc.getTrangThai().equals("Đã duyệt")){
                                        lstTinTuc.add(tinTuc);
                                    }
                                }
                                // Cập nhật RecyclerView
                                tinTucAdapter.notifyDataSetChanged();
                            } else {
                                // Xử lý khi có lỗi xảy ra
                            }
                        }
                    });
        }else{
            FirebaseFirestore.getInstance().collection("TinTuc")
                    .orderBy("NgayDang", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                    TinTuc tinTuc = new TinTuc();
                                    tinTuc.setTenBaiBao(document.getString("TenBaiBao"));
                                    tinTuc.setNoiDung(document.getString("NoiDung"));
                                    tinTuc.setIDBaiBao(document.getId());
                                    tinTuc.setAnh(document.getString("AnhBia"));
                                    tinTuc.setTacGia(document.getString("TacGia"));
//                                  tinTuc.setLinkBaiBao(document.get("LinkTinTuc").toString());
                                    tinTuc.setIDDanhMuc(document.getString("DanhMuc"));
                                    tinTuc.setSoLuotXem(Integer.parseInt(document.get("LuotXem").toString()));
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
                                    if(tinTuc.getTrangThai().equals("Đã duyệt")){
                                        lstTinTuc.add(tinTuc);
                                    }
                                }
                                // Cập nhật RecyclerView
                                tinTucAdapter.notifyDataSetChanged();
                            } else {
                                // Xử lý khi có lỗi xảy ra
                            }
                        }
                    });
        }
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        tinTucAdapter = new com.example.appnews.adapter.TinTucAdapter(lstTinTuc, (Context) activity, link, this);
        rvTinTuc.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvTinTuc.setAdapter(tinTucAdapter);
    }
    private String idTinTuc="";
    @Override
    public void onItemClick(String id) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("History", Context.MODE_PRIVATE);
        Intent intent = new Intent(getActivity(), DocTinTucActivity.class);
        String[] fixInput = id.split("IDBaiBao:");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newsRef = db.collection("TinTuc").document(fixInput[1]);
        newsRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        luotXem = documentSnapshot.getLong("LuotXem");
                    } else {

                    }
                })
                .addOnFailureListener(e -> {
                });
        luotXem++;
        Toast.makeText(getActivity(), "SoLuotXem: "+luotXem,Toast.LENGTH_SHORT).show();
        newsRef.update("LuotXem", luotXem)
                .addOnSuccessListener(aVoid -> {
                    // Nếu cập nhật thành công, cập nhật giao diện người dùng
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi cập nhật không thành công (nếu cần)
                });
        String idTT = sharedPreferences.getString("ID", "");
        ArrayList<String> idTinTuc2 = new ArrayList<>();
        String[] listID = idTT.split("/");
        for (String value : listID) {
            if (value != null){
                idTinTuc2.add(value);
            }
        }
        int d = 0;
        if (idTinTuc2.size() != 0){
            for (int i = 0; i < idTinTuc2.size(); i++){
                if (Objects.equals(idTinTuc2.get(i), fixInput[1])){
                    d++;
                }
            }
            if (d == 0){
                idTinTuc = idTT +"/"+ fixInput[1];
            } else {
                idTinTuc = idTT ;
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID", idTinTuc);
        editor.apply();
        intent.putExtra("IDTinTuc", fixInput[1]);
        startActivity(intent);
    }
}