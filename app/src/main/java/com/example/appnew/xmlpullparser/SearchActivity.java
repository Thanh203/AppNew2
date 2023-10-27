package com.example.appnew.xmlpullparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.adapter.TinTucAdapter;
import com.example.appnew.enity.TinTuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements ItemCallback {
    SearchView searchView;
    List<TinTuc> lstTinTuc;
    TinTucAdapter tinTucAdapter;
    RecyclerView rvSearch;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchtrangtru);
        rvSearch = findViewById(R.id.rvsearch);
        lstTinTuc = new ArrayList<>();
        getDataFromFirestore();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        tinTucAdapter = new TinTucAdapter(lstTinTuc, this, link, this);
        rvSearch.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvSearch.setAdapter(tinTucAdapter);

    } private void filterList(String newText) {
        List<TinTuc> filteredList   = new ArrayList<>();
        for(TinTuc tinTuc : lstTinTuc)
        {
            if(tinTuc.getTenBaiBao().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(tinTuc);
            }
        }
        if (filteredList.isEmpty())
        {
            Toast.makeText(this,"Không tìm thấy",Toast.LENGTH_SHORT).show();
        }
        else
        {
            tinTucAdapter.setFilteredList(filteredList);

        }
    }
    private void getDataFromFirestore() {
        FirebaseFirestore.getInstance().collection("TinTuc")
                .orderBy("NgayDang", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot querySnapshot = task.getResult();

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Chuyển đổi dữ liệu từ Firestore thành đối tượng YourObject
                                TinTuc tinTuc = new TinTuc();
                                tinTuc.setTenBaiBao(document.get("TenBaiBao").toString());
                                tinTuc.setNoiDung(document.get("NoiDung").toString());
                                tinTuc.setIDBaiBao(document.getId());
                                tinTuc.setAnh(document.get("AnhBia").toString());
                                tinTuc.setTacGia(document.get("TacGia").toString());
//                                  tinTuc.setLinkBaiBao(document.get("LinkTinTuc").toString());
                                tinTuc.setIDDanhMuc(document.get("DanhMuc").toString());


                                Timestamp timestamp = document.getTimestamp("NgayDang");
                                if (timestamp != null) {
                                    // Lấy giá trị thời gian từ Timestamp
                                    long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;

                                    // Tạo đối tượng Date từ milliseconds
                                    Date date = new Date(milliseconds);

                                    // Sử dụng đối tượng Date cho các mục đích khác nhau trong mã của bạn
                                    // Ví dụ:
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                    String formattedTimestamp = dateFormat.format(date);

                                    tinTuc.setNgayDang(formattedTimestamp);

                                }

                                lstTinTuc.add(tinTuc);
                            }
                            // Cập nhật RecyclerView
                            tinTucAdapter.notifyDataSetChanged();
                        } else {
                            // Xử lý khi có lỗi xảy ra
                        }
                    }
                });
    }


    @Override
    public void onItemClick(String id) {

    }
}