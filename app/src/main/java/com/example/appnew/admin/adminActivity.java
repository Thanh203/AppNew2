package com.example.appnew.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.login.LoginActivity;
import com.example.appnew.admin.ThongTinTaiKhoanFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class adminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_DANHMUC = 1;
    private static final int FRAGMENT_TINTUC = 2;
    private static final int FRAGMENT_USER = 3;
    private static final int FRAGMENT_PHANQUYEN = 4;
    private static final int FRAGMENT_PHEDUYET = 5;
    public static final int MY_REQUEST_CODE = 10;
    private  int mCurrentFragment = FRAGMENT_HOME;
    private DrawerLayout mDrawerLayout;

    String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String hasPermission = checkPermission();
        int id = item.getItemId();
        if (id == R.id.nav_trangchu_admin){

            if (mCurrentFragment != FRAGMENT_HOME){
//                loadFragment(new HomeAdminFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }

        } else if (id == R.id.nav_danhmuc_admin){
            if (mCurrentFragment != FRAGMENT_DANHMUC){
                loadFragment(new DanhMucAdminFragment());
                mCurrentFragment = FRAGMENT_DANHMUC;
            }
        } else if (id == R.id.nav_dangxuat_admin){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_tintuc_admin) {
            if (mCurrentFragment != FRAGMENT_TINTUC) {
                loadFragment(new TinTucFragment());
                mCurrentFragment = FRAGMENT_TINTUC;
            }
        }

         else if (id == R.id.nav_user_admin) {
            if (mCurrentFragment != FRAGMENT_USER) {
                loadFragment(new ThongTinTaiKhoanFragment());
                mCurrentFragment = FRAGMENT_USER;
            }
        }
        else if (id == R.id.nav_phanquyen_admin)
    {
        if (hasPermission.equals("admin0")) {
            if (mCurrentFragment != FRAGMENT_PHANQUYEN) {
                loadFragment(new PhanQuyenFragment());
                mCurrentFragment = FRAGMENT_PHANQUYEN;
            }
        } else {
            item.setEnabled(false);
            item.setIcon(R.drawable.baseline_lock_24);
            Toast.makeText(this, "Bạn không đủ quyền hạn", Toast.LENGTH_SHORT).show();
        }
    }
        else if (id == R.id.nav_pheduyet_admin){
            if(hasPermission.equals("admin0")){
                if (mCurrentFragment != FRAGMENT_PHEDUYET){
                    loadFragment(new PheDuyetFragment());
                    mCurrentFragment = FRAGMENT_PHEDUYET;
                }
            }else {
                item.setEnabled(false);
                item.setIcon(R.drawable.baseline_lock_24);
                Toast.makeText(this, "Bạn không đủ quyền hạn", Toast.LENGTH_SHORT).show();
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private String checkPermission() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                permission = bundle.getString("Permission");
            }
        }
        return permission;
    }
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    void loadFragment(Fragment fmNew)
    {
        FragmentTransaction fmThanh = getSupportFragmentManager().beginTransaction();
        fmThanh.replace(R.id.fragment_container, fmNew);
        fmThanh.addToBackStack(null);
        fmThanh.commit();
    }
}