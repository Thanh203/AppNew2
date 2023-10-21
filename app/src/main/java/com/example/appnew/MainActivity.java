package com.example.appnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appnew.fragment.DanhMucFragment;
import com.example.appnew.fragment.MenuFragment;
import com.example.appnew.fragment.TinHotFragment;
import com.example.appnew.fragment.TrangChuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity{
    private BottomNavigationView mnBottom;
    public static final int MY_REQUEST_CODE = 10;
    public static final int MENU_TRANG_CHU = R.id.mnTrangChu;
    public static final int MENU_DANH_MUC = R.id.mnDanhMuc;
    public static final int MENU_TIN_HOT = R.id.mnTinHot;
    public static final int MENU_MENU = R.id.mnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mnBottom = findViewById(R.id.navMenu);
        mnBottom.setOnItemSelectedListener(getListener());
        loadFragment(new TrangChuFragment());
    }
    @NonNull
    private NavigationBarView.OnItemSelectedListener getListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == MENU_TRANG_CHU) {
                    // Xử lý Trang Chủ
                    loadFragment(new TrangChuFragment());
                    return true;
                } else if (item.getItemId() == MENU_DANH_MUC) {
                    // Xử lý Danh Mục
                    loadFragment(new DanhMucFragment());
                    return true;
                } else if (item.getItemId() == MENU_TIN_HOT) {
                    // Xử lý Tin Hot
                    loadFragment(new TinHotFragment());
                    return true;
                } else if (item.getItemId() == MENU_MENU) {
                    // Xử lý Menu
                    loadFragment(new MenuFragment());
                    return true;
                }
                mnBottom.setSelectedItemId(item.getItemId());
                return true;
            }
        };
    }
    void loadFragment(Fragment fmNew)
    {
        FragmentTransaction fmThanh = getSupportFragmentManager().beginTransaction();
        fmThanh.replace(R.id.main_fragment, fmNew);
        fmThanh.addToBackStack(null);
        fmThanh.commit();
    }
}