package com.example.appnew.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appnew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DanhMucAdminFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_danh_muc_admin, container, false);
        loadFragment( new DmNew24hFragment());
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bn_DanhMucAD);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = new DmNew24hFragment();

//                switch (item.getItemId()) {
//                    case R.id.action_news24h:
//                        selectedFragment = new DmNew24hFragment();
//                        break;
//                    case R.id.action_vnexpress:
//                        selectedFragment = new DmVnFragment();
//                        break;
//                }

                loadFragment(selectedFragment);
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
