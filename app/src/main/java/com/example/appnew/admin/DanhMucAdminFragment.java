package com.example.appnew.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appnew.R;
import com.example.appnew.fragment.DanhMucFragment;
import com.example.appnew.fragment.TrangChuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DanhMucAdminFragment extends Fragment{
    public static final int ACTION_NEW24H = R.id.action_new24h;
    public static final int ACTION_VNEXPRESS = R.id.action_vnexpress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_danh_muc_admin, container, false);
        loadFragment( new DmNew24hFragment());
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bn_DanhMucAD);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == ACTION_NEW24H) {
                    loadFragment(new DmNew24hFragment());
                    return true;
                } else if (item.getItemId() == ACTION_VNEXPRESS) {
                    // Xử lý Danh Mục
                    loadFragment(new DmVnFragment());
                    return true;
                }
                return true;
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.danhMucAd_fragment, fragment)
                .commit();
    }
}
