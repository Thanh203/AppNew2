package com.example.appnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnew.R;
import com.example.appnew.enity.DanhMuc;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.DanhMucVH> {
    Context context;
    List<DanhMuc> list;
    ItemCallback itemCallback;
    public DanhMucAdapter(List<DanhMuc> list, Context context, ItemCallback itemCallback) {
        this.context = context;
        this.list = list;
        this.itemCallback = itemCallback;
    }
    @NonNull
    @Override
    public DanhMucVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layoutitemgrid, parent, false);
        return new DanhMucVH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DanhMucVH holder, int position) {
        DanhMuc danhMuc =  list.get(position);

        holder.tv_name.setText(danhMuc.getTenDanhMuc());
        Picasso.get()
                .load(danhMuc.getAnh())
                .into(holder.imAvartar);
        holder.itemView.setOnClickListener(view -> itemCallback.onItemClick("IDDanhMuc :" + String.valueOf(danhMuc.getIDDanhMuc())));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class DanhMucVH extends RecyclerView.ViewHolder{
        TextView tv_name;
        ImageView imAvartar;
        public DanhMucVH(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById((R.id.tv_name));
            imAvartar = itemView.findViewById(R.id.ivAvartar);
        }
    }
}
