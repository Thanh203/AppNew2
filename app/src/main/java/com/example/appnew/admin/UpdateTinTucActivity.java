package com.example.appnew.admin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appnew.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateTinTucActivity extends AppCompatActivity {

    private EditText edtTenBaiBao, edtTacGia, edtAnhBia, edtNoiDung, edtDanhMuc;
    private Button btnEdit, btnBack;
    FirebaseFirestore db;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tin_tuc);
        AnhXa();

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            id = bundle.getString("IDTinTuc");
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cập nhật nội dung tài liệu
                Map<String, Object> updatedData = new HashMap<>();
                String trangThai = "Chưa duyệt";
                updatedData.put("TenBaiBao", edtTenBaiBao.getText().toString());
                updatedData.put("AnhBia", edtAnhBia.getText().toString());
                updatedData.put("IDDanhMuc", edtDanhMuc.getText().toString());
                updatedData.put("TacGia", edtTacGia.getText().toString());
                updatedData.put("NoiDung", edtNoiDung.getText().toString());
                updatedData.put("TrangThai", trangThai);

                db.collection("TinTuc")
                        .document(id)
                        .update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            // Thực hiện thành công
                            Toast.makeText(UpdateTinTucActivity.this, "Cập nhật tin tức thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi
                            Toast.makeText(UpdateTinTucActivity.this, "Cập nhật tin tức không thành công", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getDataTinTuc(id);

    }

    private void getDataTinTuc(String id){
        db.collection("TinTuc")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Lấy thông tin từ tài liệu
                            edtTenBaiBao.setText(document.getString("TenBaiBao"));
                            edtAnhBia.setText(document.getString("AnhBia"));
                            edtDanhMuc.setText(document.getString("DanhMuc"));
                            edtTacGia.setText(document.getString("TacGia"));
                            edtNoiDung.setText(document.getString("NoiDung"));
                        } else {
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.d(TAG, "Error getting document: ", task.getException());
                    }
                });
    }
    private void AnhXa(){
        edtTenBaiBao = findViewById(R.id.edt_TenBaiBao);
        edtTacGia = findViewById(R.id.edt_TacGia);
        edtAnhBia = findViewById(R.id.edt_AnhBia);
        edtNoiDung = findViewById(R.id.edt_NoiDung);
        edtDanhMuc = findViewById(R.id.edt_DanhMuc);
        btnEdit = findViewById(R.id.btn_editTinTuc);
        btnBack = findViewById(R.id.btn_back);
    }
}

