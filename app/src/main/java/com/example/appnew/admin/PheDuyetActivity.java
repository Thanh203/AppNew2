package com.example.appnew.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnew.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PheDuyetActivity extends AppCompatActivity {

    ImageButton imBackDocBaoC;
    TextView tvTacGiaC,tvNoidungC,tvTenbaibaoC,tvNgayDangC;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imBaoC;
    String idTinTuc;
    String linkImg;
    Button btnPheDuyet, btnKoPheDuyet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phe_duyet);
        AnhXa();

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleStyle);
        imBackDocBaoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idTinTuc = bundle.getString("IDTinTuc");

        final String[] trangThai = {""};
        Map<String, Object> updatedData = new HashMap<>();

        btnKoPheDuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] trangThai = {""};
                Map<String, Object> updatedData = new HashMap<>();
                trangThai[0] = "Duyệt thất bại";
                updatedData.put("TrangThai", trangThai[0]);
                FirebaseFirestore.getInstance().collection("TinTuc")
                        .document(idTinTuc)
                        .update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            // Thực hiện thành công
                            Toast.makeText(PheDuyetActivity.this, "Phê duyệt thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi
                            Toast.makeText(PheDuyetActivity.this, "Phê duyệt không thành công", Toast.LENGTH_SHORT).show();
                        });
            }
        });
        btnPheDuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] trangThai = {""};
                Map<String, Object> updatedData = new HashMap<>();
                trangThai[0] = "Đã duyệt";
                updatedData.put("TrangThai", trangThai[0]);
                FirebaseFirestore.getInstance().collection("TinTuc")
                        .document(idTinTuc)
                        .update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            // Thực hiện thành công
                            Toast.makeText(PheDuyetActivity.this, "Phê duyệt thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi
                            Toast.makeText(PheDuyetActivity.this, "Phê duyệt không thành công", Toast.LENGTH_SHORT).show();
                        });
            }
        });



        getDataTinTuc();
    }
    private void AnhXa(){
        imBackDocBaoC = findViewById(R.id.imgback_tintuc_PD);
        tvTacGiaC = findViewById(R.id.tv_tacgiaPD);
        tvNoidungC = findViewById(R.id.tv_noidungPD);
        tvNgayDangC = findViewById(R.id.tv_ngaydangPD);
        collapsingToolbarLayout = findViewById(R.id.collapsingPD);
        tvTenbaibaoC = findViewById(R.id.tv_tenbaibaoPD);
        imBaoC = findViewById(R.id.img_tintucPD);
        btnPheDuyet = findViewById(R.id.btn_PheDuyet);
        btnKoPheDuyet = findViewById(R.id.btn_KoPheDuyet);

    }
    private void getDataTinTuc() {
        FirebaseFirestore.getInstance().collection("TinTuc")
                .document(idTinTuc)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tvTenbaibaoC.setText(document.getString("TenBaiBao"));
                            tvNoidungC.setText(document.getString("NoiDung"));
                            tvTacGiaC.setText(document.getString("TacGia"));
                            if (document.contains("NgayDang")) {
                                Date ngayDang = document.getDate("NgayDang");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String ngayDangFormatted = dateFormat.format(ngayDang);
                                tvNgayDangC.setText(ngayDangFormatted);
                            } else {
                                // Xử lý khi không có trường NgayDang trong document
                                tvNgayDangC.setText("");
                            }
                            linkImg = document.getString("AnhBia");
                            Picasso.get()
                                    .load(linkImg)
                                    .into(imBaoC);
                        }
                    }
                });
    }
}