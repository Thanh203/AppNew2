package com.example.appnew.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.enity.DanhMuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ThemTinTucActivity extends AppCompatActivity {

    private EditText edtTenBaiBao, edtTacGia, edtNoiDung, edtDanhMuc;
    private Button btnAddNew, btnBack, btnAddImg;
    Spinner spnDanhMuc;
    FirebaseFirestore db;
    ImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    List<String> lstTenDanhMuc;
    private FirebaseFirestore firestore;
    private String urlImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tin_tuc);

        AnhXa();

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        lstTenDanhMuc = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("DanhMuc")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DanhMuc danhMuc = new DanhMuc();
                                danhMuc.setTenDanhMuc(document.getString("TenDanhMuc"));
                                danhMuc.setAnh(document.getString("Anh"));
                                danhMuc.setLink(document.getString("Link"));
                                if (danhMuc.getLink().equals("")){
                                    lstTenDanhMuc.add(danhMuc.getTenDanhMuc());
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemTinTucActivity.this, android.R.layout.simple_spinner_item, lstTenDanhMuc);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnDanhMuc.setAdapter(adapter);
                        } else {
                            Log.d("Firestore", "Error getting categories: " + task.getException());
                        }
                    }


                });

        db = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Tiếp tục với việc tải lên Cloud Storage và thêm đường liên kết vào Firestore
            uploadImageToStorageAndAddToFirestore(selectedImageUri);
        }
    }
    private void uploadImageToStorageAndAddToFirestore(Uri imageUri) {
        // Tạo tham chiếu đến Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Tạo tên file duy nhất cho ảnh
        String filename = UUID.randomUUID().toString();

        // Tham chiếu đến file trong Firebase Storage
        StorageReference imageRef = storageRef.child("imagesTinTuc/" + filename);

        // Tải ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy đường liên kết ảnh
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Lưu đường liên kết ảnh vào Firestore
                                displayImage(uri.toString());
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý lỗi
                            });
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });
    }
    private void displayImage(String imageUrl) {
        // Sử dụng thư viện hỗ trợ như Picasso hoặc Glide để tải và hiển thị ảnh lên ImageView
        Picasso.get()
                .load(imageUrl)
                .into(img);

        urlImg = imageUrl;
    }
    private void insertData(){

        String selectedCategory = spnDanhMuc.getSelectedItem().toString();
        String trangThai = "Đã duyệt";
        Map<String, Object> map = new HashMap<>();
        map.put("TenBaiBao", edtTenBaiBao.getText().toString());
        map.put("TacGia", edtTacGia.getText().toString());
        map.put("NoiDung", edtNoiDung.getText().toString());
        map.put("AnhBia", urlImg);
        map.put("DanhMuc", selectedCategory);
        Timestamp timestamp = Timestamp.now();
        map.put("NgayDang", timestamp);
        map.put("TrangThai", trangThai);
        map.put("LuotXem", 0);

        db.collection("TinTuc")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ThemTinTucActivity.this, "Thêm tin tức thành công", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThemTinTucActivity.this, "Lỗi khi thêm tin tức: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void AnhXa(){
        edtTenBaiBao = findViewById(R.id.edt_TenBaiBao);
        edtTacGia = findViewById(R.id.edt_TacGia);
        edtNoiDung = findViewById(R.id.edt_NoiDung);
        edtDanhMuc = findViewById(R.id.edt_DanhMuc);
        img = findViewById(R.id.img_TinTuc);
        spnDanhMuc = findViewById(R.id.categorySpinner);

        btnAddNew = findViewById(R.id.btn_addNew);
        btnBack = findViewById(R.id.btn_back);
        btnAddImg = findViewById(R.id.btn_addImage);

    }
}