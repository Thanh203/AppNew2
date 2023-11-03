package com.example.appnew.fragment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appnew.CalenderActivity;
import com.example.appnew.FootlballActivity;
import com.example.appnew.LichSuActivity;
import com.example.appnew.R;
import com.example.appnew.ThoiTietActivity;
import com.example.appnew.ThongTinTaiKhoanActivity;
import com.example.appnew.ViewFormActivity;
import com.example.appnew.enity.TaiKhoan;
import com.example.appnew.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }
    private ImageView imAvatar;
    private TextView tvName, tvLichSu;
    private TextView tvEmail;
    private TextView tvDangXuat;
    private TextView tvTTTK;
    private TextView tvDangkybieumauC;
    TaiKhoan taiKhoan;
    private TextView login_menufragment,weatherTextView,vietnameseCalendarTextView,FoolballTextView;
    private Switch aSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean nightMODE;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        imAvatar = activity.findViewById(R.id.img_avatar);
        tvName = activity.findViewById(R.id.tv_name);
        tvEmail = activity.findViewById(R.id.tv_email);
        tvDangXuat = activity.findViewById(R.id.tv_DangXuat);
        tvTTTK = activity.findViewById(R.id.tv_TTTK);
        tvDangkybieumauC = activity.findViewById(R.id.dangkybieumauTextView);
        tvLichSu = activity.findViewById(R.id.tv_LichSu);
        weatherTextView = activity.findViewById(R.id.weatherTextView);
        aSwitch = activity.findViewById(R.id.switcher);
        vietnameseCalendarTextView = activity.findViewById(R.id.vietnameseCalendarTextView);
        FoolballTextView = activity.findViewById(R.id.FoolballTextView);
        sharedPreferences = activity.getSharedPreferences("MODE", Context.MODE_PRIVATE);//tao file mode (neu chua co), neu co goi file ra
        //MODE_PRIVATE đây là chế độ lưu riêng tư không cho phép ứng dụng khác truy cập vào đc hoặc đọc từ ứng dụng khác
        nightMODE = sharedPreferences.getBoolean("night", false);// Truy xuất giá trị từ tệp
        if (nightMODE){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE){
                    //AppCompatDelegate la phuong thuc tich hop san de tuy chinh che do night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor =sharedPreferences.edit();//mo file ra ghi noi dung vao
                    editor.putBoolean("night", false);// mở key night lưu lại giá trị
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor =sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            tvDangXuat.setText("   Đăng nhập");
            tvDangXuat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            tvDangXuat.setText("   Đăng xuất");
            tvDangXuat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        showUserInformation();
        weatherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ThoiTietActivity.class);
                startActivity(intent);
            }
        });
        FoolballTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FootlballActivity.class);
                startActivity(intent);
            }
        });
        vietnameseCalendarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CalenderActivity.class);
                startActivity(intent);
            }
        });
        tvLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LichSuActivity.class);
                startActivity(intent);
            }
        });

        tvTTTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ThongTinTaiKhoanActivity.class);
                startActivity(intent);
            }
        });


        tvDangkybieumauC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogbottom();

            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            getDataUser();
        }
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
                            taiKhoan = new TaiKhoan();
                            taiKhoan.setHoTen(document.getString("HoTen"));
                            taiKhoan.setEmail(document.getString("Email"));
                            taiKhoan.setAnh(document.getString("Anh"));

                            tvName.setText(taiKhoan.getHoTen());
                            tvEmail.setText(taiKhoan.getEmail());
                            Picasso.get()
                                    .load(taiKhoan.getAnh())
                                    .into(imAvatar);
                        } else {
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.d(TAG, "Error getting document: ", task.getException());
                    }
                });
    }
    private void showdialogbottom() {
        final Dialog dialog = new Dialog(getContext());
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        LinearLayout dangtinLayout = dialog.findViewById(R.id.layoutdkdangtin);
        LinearLayout nguoithanLayout = dialog.findViewById(R.id.layoutTimnguoithan);
        LinearLayout toasoanLayout = dialog.findViewById(R.id.layoutTQtoasoan);
        TextView DKdangtin = dialog.findViewById(R.id.tvDKDangtin);
        TextView DKtimnguoithan = dialog.findViewById(R.id.tvDKtimnguoithan);
        TextView DKthamquan = dialog.findViewById(R.id.tvDKTQToasoan);
        DKdangtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSfuKRLsZXFOJ8S3oakeDXDTKq0XAXScVJJMHU-T6znMbGz05Q/viewform?usp=sf_link";
                Intent intent = new Intent(getActivity(), ViewFormActivity.class);
                intent.putExtra("linknews", url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        DKtimnguoithan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSdb1LLWnuzk1oGqSiJcBPPogRWhoKIH6I4s5Fo7yJAtmsgW5g/viewform?pli=1";
                Intent intent = new Intent(getActivity(), ViewFormActivity.class);
                intent.putExtra("linknews", url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        DKthamquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLScgmYFj2NTmq53VWzNSA4OKAuxizD9UxYiaXVok_S_2k3favg/viewform";
                Intent intent = new Intent(getActivity(), ViewFormActivity.class);
                intent.putExtra("linknews", url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        if (name == null){
            tvName.setVisibility(View.GONE);
        }else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_defaut).into(imAvatar);

    }

}