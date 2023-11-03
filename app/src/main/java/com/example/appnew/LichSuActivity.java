package com.example.appnew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.adapter.LichSuApdapter;
import com.example.appnew.enity.TinTuc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LichSuActivity extends AppCompatActivity implements ItemCallback{
    RecyclerView rvLichSu;
    LichSuApdapter tinTucAdapter;
    private  List<TinTuc> lstTinTuc;
    String link;
    ImageButton btnDeleteAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);
        rvLichSu = findViewById(R.id.rv_LichSu);
        btnDeleteAll = findViewById(R.id.ib_deleteAll);
        lstTinTuc = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toobarLS);
        toolbar.setTitle("Lịch sử");
        setSupportActionBar(toolbar);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("History", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("ID");
                editor.apply();
            }
        });
        // Đọc tin tức từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("History", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("ID", "");
        // Tạo một danh sách các tin tức
        lstTinTuc = new ArrayList<>();
        ArrayList<String> idTinTuc = new ArrayList<>();
        String[] listID = id.split("/");
        for (String value : listID) {
            if (value != null){
                idTinTuc.add(value);
            }
        }
        if (idTinTuc.size() != 0){
            for (int i = 1; i < idTinTuc.size(); i++){
                FirebaseFirestore.getInstance().collection("TinTuc")
                        //.orderBy("NgayDang", Query.Direction.DESCENDING)
                        .document(String.valueOf(idTinTuc.get(i)))
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot document) {
                                if (document.exists()) {
                                    TinTuc tinTuc = new TinTuc();
                                    tinTuc.setIDBaiBao(document.getId());
                                    tinTuc.setTenBaiBao(Objects.requireNonNull(document.get("TenBaiBao")).toString());
                                    tinTuc.setNoiDung(Objects.requireNonNull(document.get("NoiDung")).toString());
                                    tinTuc.setAnh(Objects.requireNonNull(document.get("AnhBia")).toString());
                                    tinTuc.setTacGia(Objects.requireNonNull(document.get("TacGia")).toString());
//                                  tinTuc.setLinkBaiBao(document.get("LinkTinTuc").toString());
                                    tinTuc.setIDDanhMuc(Objects.requireNonNull(document.get("DanhMuc")).toString());

                                    Timestamp timestamp = document.getTimestamp("NgayDang");
                                    if (timestamp != null) {
                                        // Lấy giá trị thời gian từ Timestamp
                                        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;
                                        // Tạo đối tượng Date từ milliseconds
                                        Date date = new Date(milliseconds);
                                        // Sử dụng đối tượng Date cho các mục đích khác nhau trong mã của bạn
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                        String formattedTimestamp = dateFormat.format(date);
                                        tinTuc.setNgayDang(formattedTimestamp);
                                    }
                                    lstTinTuc.add(tinTuc);
                                    // Cập nhật RecyclerView
                                    tinTucAdapter.notifyDataSetChanged();
                                    } else {
                                    // Không tìm thấy tài liệu với ID cung cấp
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu
                            }
                        });
            }
            tinTucAdapter = new LichSuApdapter(lstTinTuc, this, link,  this);
            rvLichSu.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            rvLichSu.setAdapter(tinTucAdapter);
        }
        else{
            String t = "Lịch sử trống";
            Toast.makeText(LichSuActivity.this, t, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(this, DocTinTucActivity.class);
        String[] fixInput = id.split("IDBaiBao:");
        intent.putExtra("IDTinTuc", fixInput[1]);
        startActivity(intent);
    }
}