package com.example.appnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnew.adapter.ItemCallback;
import com.example.appnew.enity.DanhMuc;
import com.example.appnew.R;
import com.example.appnew.enity.DanhMuc;

import java.util.List;

public class GoiDanhMucAdapter extends RecyclerView.Adapter<GoiDanhMucAdapter.GoiYVH>{
    List<DanhMuc> lstDanhMuc;
    Context context;
    ItemCallback itemCallback;

    public GoiDanhMucAdapter(List<DanhMuc> lstGoiY, Context context , ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
        this.lstDanhMuc = lstGoiY;
        this.context = context;
    }

    @NonNull
    @Override
    public GoiYVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layoutitemdanhmuc, parent, false);
        return new GoiYVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoiYVH holder, int position) {
        DanhMuc danhMuc =  lstDanhMuc.get(position);

        holder.tvDanhMuc.setText(danhMuc.getTenDanhMuc());


        holder.itemView.setOnClickListener(view -> itemCallback.onItemClick("Link:" + String.valueOf(danhMuc.getLink())));
    }

    @Override
    public int getItemCount() {
        return lstDanhMuc.size();
    }

    static class GoiYVH extends RecyclerView.ViewHolder{
        TextView tvDanhMuc;
        public GoiYVH(@NonNull View itemView) {
            super(itemView);
            tvDanhMuc = itemView.findViewById((R.id.tvDanhMuc));
        }
    }
}
