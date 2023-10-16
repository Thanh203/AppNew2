package com.example.appnew.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appnew.MainActivity;
import com.example.appnew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageButton imageButtonC;
    EditText edPasswordReC,edConfirmPasswordReC,edEmailReC;
    Button btnRegisterC;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        imageButtonC = findViewById(R.id.imBack);
        edPasswordReC = findViewById(R.id.edpassRe);
        edConfirmPasswordReC = findViewById(R.id.edconfirmpassRe);
        edEmailReC = findViewById(R.id.edemaiRe);
        btnRegisterC=findViewById(R.id.btRegister);

        imageButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        btnRegisterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edPasswordReC.getText().toString().trim();
                String confrimpassword = edConfirmPasswordReC.getText().toString().trim();
                String email = edEmailReC.getText().toString().trim();
                boolean isValid = CheckPassWord(password, confrimpassword) && CheckEmail(email);
                if (isValid){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            // Tạo một document mới trong collection "users"
                                            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid());
                                            // Tạo đối tượng User
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("Password", password);
                                            user.put("Email", email);
                                            user.put("VaiTro", "user");
                                            // Thêm dữ liệu vào Firestore
                                            userRef.set(user)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Thêm dữ liệu thành công
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getApplicationContext(), "đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                                    });

                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(getApplicationContext(), "đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finishAffinity();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
    private boolean CheckPassWord(String password, String confrimpassword) {
        if(password.isEmpty()){
            edPasswordReC.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }
        if(password.length() <= 5){
            edPasswordReC.setError("Mật khẩu phải lớn hơn 5 ký tự");
            return false;}
        if(!password.equals(confrimpassword)){
            edConfirmPasswordReC.setError("xac nhan mat khau khong khop");
            return false;
        }
        return true;
    }

    private boolean CheckEmail(String email){
        if (email.isEmpty()){
            edEmailReC.setError("Email không được bỏ trống");
            return false;
        }
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmailReC.setError("Email không hợp lệ");
            return false;
        }
        return true;
    }
}