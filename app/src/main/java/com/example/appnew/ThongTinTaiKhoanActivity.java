package com.example.appnew;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ThongTinTaiKhoanActivity extends AppCompatActivity {

    private EditText edtHoVaTen, edtEmail, edtDay, edtMonth, edtYear, edtNgheNghiep,edtCCCD, edtSDT;
    private ImageView imgAvatar;
    private Button btnCapNhap;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    String ngaySinh, gioiTinh, linkAnh;
    private Button birthdayButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String urlImg;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);

        AnhXa();

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.maleRadioButton) {
                    // Nút lựa chọn "Nam" được chọn
                    gioiTinh = "Nam";
                } else if (checkedId == R.id.femaleRadioButton) {
                    // Nút lựa chọn "Nữ" được chọn
                    gioiTinh = "Nữ";
                }
            }
        });

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Khởi tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinTaiKhoanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Lưu giá trị ngày sinh vào biến NgaySinh
                                ngaySinh = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                // Hiển thị ngày sinh đã chọn lên màn hình (ví dụ: TextView)
                                edtDay.setText(String.valueOf(dayOfMonth));
                                edtMonth.setText(String.valueOf(monthOfYear));
                                edtYear.setText(String.valueOf(year));

                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        getDataUser();

        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("HoTen", edtHoVaTen.getText().toString());
                updatedData.put("NgheNghiep", edtNgheNghiep.getText().toString());
                updatedData.put("CCCD", edtCCCD.getText().toString());
                updatedData.put("SDT", edtSDT.getText().toString());
                updatedData.put("GioiTinh", gioiTinh);
                updatedData.put("NgaySinh", ngaySinh);
                updatedData.put("Anh", urlImg);


                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(currentUser.getUid())
                        .update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            // Thực hiện thành công
                            Log.d(TAG, "Document updated successfully");
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi
                            Log.e(TAG, "Error updating document", e);
                        });

            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
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
        StorageReference imageRef = storageRef.child("imagesUsers/" + filename);

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
                .into(imgAvatar);

        urlImg = imageUrl;
    }
    private void getDataUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Lấy thông tin từ tài liệu
                            edtHoVaTen.setText(document.getString("HoTen"));
                            edtEmail.setText(document.getString("Email"));
                            edtNgheNghiep.setText(document.getString("NgheNghiep"));
                            edtCCCD.setText(document.getString("CCCD"));
                            edtSDT.setText(document.getString("SDT"));
                            gioiTinh = document.getString("GioiTinh");

                            if (Objects.equals(gioiTinh, "Nam")){
                                maleRadioButton.setChecked(true);
                            }else if (Objects.equals(gioiTinh, "Nữ")){
                                femaleRadioButton.setChecked(true);
                            }

                            ngaySinh = document.getString("NgaySinh");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                            try {
                                Date date = dateFormat.parse(ngaySinh);

                                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

                                String day = dayFormat.format(date);
                                String month = monthFormat.format(date);
                                String year = yearFormat.format(date);

                                edtDay.setText(day);
                                edtMonth.setText(month);
                                edtYear.setText(year);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            linkAnh = document.getString("Anh");
                            Picasso.get()
                                    .load(linkAnh)
                                    .into(imgAvatar);

                        } else {
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.d(TAG, "Error getting document: ", task.getException());
                    }
                });
    }

    private void AnhXa(){

        imgAvatar = findViewById(R.id.im_Avatar);
        edtHoVaTen = findViewById(R.id.edt_HoVaTen);
        edtEmail = findViewById(R.id.edt_Email);
        edtNgheNghiep = findViewById(R.id.edt_NgheNghiep);
        edtCCCD = findViewById(R.id.edt_CCCD);
        edtSDT = findViewById(R.id.edt_SDT);

        btnCapNhap = findViewById(R.id.btn_CapNhap);

        edtEmail.setEnabled(false);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        birthdayButton = findViewById(R.id.birthdayButton);
        edtDay = findViewById(R.id.edt_Day);
        edtMonth = findViewById(R.id.edt_Month);
        edtYear = findViewById(R.id.edt_Year);

    }
}