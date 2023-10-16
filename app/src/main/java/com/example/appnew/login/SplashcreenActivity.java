package com.example.appnew.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appnew.MainActivity;
import com.example.appnew.R;
import com.example.appnew.admin.adminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashcreenActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView1 , textView2;
    Animation top,bottom;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashcreen);
        imageView = findViewById(R.id.ivLogosplash1);
        textView1 = findViewById(R.id.tvSplash);
        top = AnimationUtils.loadAnimation(this,R.anim.top);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom);
        imageView.setAnimation(top);
        textView1.setAnimation(bottom);
        //Goi giá trị trong key "night" ra
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMODE= sharedPreferences.getBoolean("night", Boolean.parseBoolean(""));
        if (nightMODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NextActivity();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Lấy thông tin người dùng từ Firestore
                    db.collection("users")
                            .document(currentUser.getUid())
                            .get()
                            .addOnCompleteListener(userTask -> {
                                if (userTask.isSuccessful()) {
                                    DocumentSnapshot document = userTask.getResult();
                                    if (document != null && document.exists()) {
                                        String role = document.getString("VaiTro");
                                        // Kiểm tra và xử lý vai trò của người dùng
                                        if ((role.equals("admin0"))||(role.equals("admin1"))||(role.equals("admin2"))) {
                                            Intent i = new Intent(SplashcreenActivity.this, adminActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Permission", role);
                                            i.putExtras(bundle);
                                            startActivity(i);
                                            finishAffinity();
                                        } else {
                                            Intent i = new Intent(SplashcreenActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finishAffinity();
                                        }
                                    }
                                }
                            });
                }
            }
        },3000);
    }
    private void NextActivity(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            Intent intent = new Intent(SplashcreenActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}