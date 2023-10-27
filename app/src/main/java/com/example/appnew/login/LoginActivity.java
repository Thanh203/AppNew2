package com.example.appnew.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnew.MainActivity;
import com.example.appnew.R;
import com.example.appnew.admin.adminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText edUserC,edPassC;
    TextView tvRegisC;
    Button btnLoginC;
    FirebaseAuth mAuth;
    ImageButton imBackC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserC = findViewById(R.id.eduserL);
        edPassC = findViewById(R.id.edpassL);
        btnLoginC = findViewById(R.id.btloginL);
        imBackC = findViewById(R.id.imBackS);
        tvRegisC = findViewById(R.id.tvregisterlogin);
        mAuth = FirebaseAuth.getInstance();
        tvRegisC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
        btnLoginC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edUserC.getText().toString().trim();
                String password = edPassC.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if (currentUser != null) {
                                    // Lấy thông tin người dùng từ Firestore
                                    db.collection("users")
                                            .document(currentUser.getUid())
                                            .get()
                                            .addOnCompleteListener(userTask -> {
                                                if (userTask.isSuccessful())
                                                {
                                                    DocumentSnapshot document = userTask.getResult();
                                                    if (document != null && document.exists()) {
                                                        String role = document.getString("VaiTro");
                                                        // Kiểm tra và xử lý vai trò của người dùng
                                                        if ((role.equals("admin0"))||(role.equals("admin1"))||(role.equals("admin2")))
                                                        {
                                                            Intent i = new Intent(LoginActivity.this, adminActivity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("Permission", role);
                                                            i.putExtras(bundle);
                                                            startActivity(i);
                                                            finishAffinity();
                                                        }
                                                        else
                                                        {
                                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                            startActivity(i);
                                                            finishAffinity();
                                                        }
                                                    }
                                                }
                                            });
                                }

                            }
                            else
                            {
                                // Đăng nhập không thành công
                                // Dang Nhap Khong thanh cong App tu dong thoat.(Test)
                            }
                        });
            }
        });
        imBackC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }
}