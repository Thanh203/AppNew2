package com.example.appnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnew.R;
import com.example.appnew.admin.UpdateTinTucActivity;
import com.example.appnew.enity.DanhMuc;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HienThiDanhMucAdminAdapter extends RecyclerView.Adapter<HienThiDanhMucAdminAdapter.myViewHolder> {

    Context context;
    List<DanhMuc> list;
    ItemCallback itemCallback;

    public HienThiDanhMucAdminAdapter(List<DanhMuc> list, Context context, ItemCallback itemCallback) {
        this.context = context;
        this.list = list;
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_danhmuc_admin, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        DanhMuc danhMuc = list.get(position);

        holder.tvDanhMuc.setText(danhMuc.getTenDanhMuc());
        Picasso.get()
                .load(danhMuc.getAnh())
                .into(holder.img);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 
                FirebaseFirestore
                        .getInstance()
                        .collection("DanhMuc").whereEqualTo(FieldPath.documentId(), danhMuc.getIDDanhMuc())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                WriteBatch a = FirebaseFirestore.getInstance().batch();
                                List<DocumentSnapshot> tinTuc = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : tinTuc) {
                                    a.delete(snapshot.getReference());
                                }
                                a.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(), "Xoá tin tức thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        });
            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(view1.getContext(), UpdateTinTucActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("IDDanhMuc", danhMuc.getIDDanhMuc());
                intent.putExtras(bundle);
                view1.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvDanhMuc;
        Button btnUpdate, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDanhMuc = itemView.findViewById(R.id.tv_name_admin);
            btnDelete = itemView.findViewById(R.id.btn_deleteDM_admin);
            btnUpdate = itemView.findViewById(R.id.btn_updateDM_admin);
            img = itemView.findViewById(R.id.ivAvartar_admin);

        }
    }
}
