package com.example.appnew.admin;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.appnew.R;
import com.example.appnew.adapter.HienThiDanhMucAdminAdapter;
import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.enity.DanhMuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DmNew24hFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DmNew24hFragment extends Fragment implements ItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DmNew24hFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DmNew24hFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DmNew24hFragment newInstance(String param1, String param2) {
        DmNew24hFragment fragment = new DmNew24hFragment();
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
    View view;
    MaterialButton btnshowC;
    Dialog dialog;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;

    private MaterialButton btnadd, exit, chossefile ;
    private EditText edtTenDanhMuc, edtLink;
    private ImageView img;
    private List<DanhMuc> lstDanhMuc;
    private HienThiDanhMucAdminAdapter adapter;

    private Uri selectedImageUri;
    String urlImg;
    RecyclerView rvDanhMuc;

    private Uri mImageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dm_new24h, container, false);

        db = FirebaseFirestore.getInstance();

        btnshowC = view.findViewById(R.id.showButton24h);
        dialog = new Dialog(getActivity());
        rvDanhMuc = view.findViewById(R.id.rcv_danhmucNew24h);

        btnshowC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });
        getDataDanhMuc();
        lstDanhMuc = new ArrayList<>();
        adapter = new HienThiDanhMucAdminAdapter(lstDanhMuc ,getActivity(), this);
        rvDanhMuc.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvDanhMuc.setAdapter(adapter);

        return view;
    }
    private void getDataDanhMuc() {
        FirebaseFirestore.getInstance().collection("DanhMuc")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                DanhMuc danhMuc = new DanhMuc();
                                danhMuc.setTenDanhMuc(document.getString("TenDanhMuc"));
                                danhMuc.setAnh(document.getString("Anh"));
                                danhMuc.setLink(document.getString("Link"));
                                danhMuc.setIDDanhMuc(document.getId());
                                if (danhMuc.getLink().equals("")){
                                    lstDanhMuc.add(danhMuc);
                                }
                            }
                            // Cập nhật RecyclerView
                            adapter.notifyDataSetChanged();
                        } else {
                            // Xử lý khi có lỗi xảy ra
                        }
                    }
                });
    }

    private void opendialog() {
        dialog.setContentView(R.layout.custom_dialog_danhmuc_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnadd = dialog.findViewById(R.id.btn_add_admin);
        exit = dialog.findViewById(R.id.btn_close_admin);
        chossefile = dialog.findViewById(R.id.bt_hinhanh_admin);
        edtTenDanhMuc = dialog.findViewById(R.id.ed_tendanhmuc_admin);
        edtLink = dialog.findViewById(R.id.ed_linkdanhmuc_admin);
        img = dialog.findViewById(R.id.image_view);
        chossefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

//        --------------------------------------------------------------------------
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("TenDanhMuc", edtTenDanhMuc.getText().toString());
                map.put("Link", edtLink.getText().toString());
                map.put("Anh", urlImg);
                db.collection("DanhMuc")
                        .add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(), "Cập nhật tin tức thành công", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Lỗi khi cập nhật tin tức: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        // --------------------------------------------------------------------------

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // --------------------------------------------------------------------------


        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        StorageReference imageRef = storageRef.child("imagesDanhMuc/" + filename);

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
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(img, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully.
                        }
                        @Override
                        public void onError(Exception e) {
                            // Handle the error when the image fails to load.
                            e.printStackTrace();
                        }
                    });

            urlImg = imageUrl;
        }
    }

    @Override
    public void onItemClick(String id) {

    }
}