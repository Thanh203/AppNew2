package com.example.appnew;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.enity.TaiKhoan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DocTinTucActivity extends AppCompatActivity {

    ImageButton imBackDocBaoC;
    TextView tvTacGiaC, tvNoidungC, tvTenbaibaoC, tvNgayDangC;
    CollapsingToolbarLayout collapsingToolbarLayout;
    EditText edCommentC;
    ImageButton imgSendCommentC;

    ImageView imBaoC, imUserC;
    String idTinTuc;
    String linkImg;

    RecyclerView RcvComment;

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    //CommentAdapter commentAdapter;
    //List<Comment> listComment;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_tin_tuc);

        imBackDocBaoC = findViewById(R.id.imgback_tintuc);
        tvTacGiaC = findViewById(R.id.tv_tacgia_docbao);
        tvNoidungC = findViewById(R.id.tv_noidung_docbao);
        edCommentC = findViewById(R.id.ed_comment);
        imUserC = findViewById(R.id.img_avatar_user);
        imgSendCommentC = findViewById(R.id.imb_send_comment);
        tvNgayDangC = findViewById(R.id.tv_ngaydang_docbao);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        tvTenbaibaoC = findViewById(R.id.tv_tenbaibao_docbao);
        RcvComment = findViewById(R.id.rv_comment);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleStyle);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            getdatauser();
        } else {
            int resourceId = R.drawable.account;
            imUserC.setImageResource(resourceId);
        }

        imBaoC = findViewById(R.id.img_tintuc_docbao);
//        tvTenbaibaoC = findViewById(R.id.tv_tenbaibao_docbao);
        imBackDocBaoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idTinTuc = bundle.getString("IDTinTuc");
        getDataTinTuc();
        //rvComment();

        imgSendCommentC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
                                    TaiKhoan taiKhoan = new TaiKhoan();
                                    taiKhoan.setHoTen(document.getString("HoTen"));
                                    taiKhoan.setEmail(document.getString("Email"));
                                    taiKhoan.setAnh(document.getString("Anh"));

                                    DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(idTinTuc).push();

                                    String comment_content = edCommentC.getText().toString();


                                    String uid = currentUser.getUid();
                                    String uname = taiKhoan.getHoTen();
                                    String uimg = taiKhoan.getAnh();

                                    /*Comment comment = new Comment(comment_content, uid, uimg, uname);
                                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showMessage("comment added");
                                            edCommentC.setText("");
                                            imgSendCommentC.setVisibility(View.VISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessage("fail to add comment : "+e.getMessage());
                                        }
                                    });*/
                                } else {
                                    Log.d(TAG, "Document does not exist");
                                }
                            } else {
                                Log.d(TAG, "Error getting document: ", task.getException());
                            }
                        });
            }
        });
    }

   /* private void rvComment() {
        RcvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(idTinTuc);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RcvComment.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    private void getdatauser() {
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
                            TaiKhoan taiKhoan = new TaiKhoan();
                            taiKhoan.setHoTen(document.getString("HoTen"));
                            taiKhoan.setEmail(document.getString("Email"));
                            taiKhoan.setAnh(document.getString("Anh"));
                            Picasso.get()
                                    .load(taiKhoan.getAnh())
                                    .into(imUserC);
                        } else {
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.d(TAG, "Error getting document: ", task.getException());
                    }
                });
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
//                            collapsingToolbarLayout.setTitle(document.getString("TenBaiBao"));
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