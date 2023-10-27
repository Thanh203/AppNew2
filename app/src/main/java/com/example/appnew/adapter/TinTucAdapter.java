package com.example.appnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnew.R;
import com.example.appnew.enity.TinTuc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.myViewHolder> {

    Context context;
    List<TinTuc> list;
    ItemCallback itemCallback;
    String link;
    private int luotXem;


    public TinTucAdapter(List<TinTuc> list, Context context, String link, ItemCallback itemCallback) {
        this.context = context;
        this.list = list;
        this.link = link;
        this.itemCallback = itemCallback;
    }

    public TinTucAdapter(Context context, ArrayList<TinTuc> list, String link) {
        this.context = context;
        this.list = list;
        this.link = link;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_newss, parent, false);
        return new myViewHolder(view);
    }
    public void setFilteredList(List<TinTuc> filteredList)
    {
        this.list = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        TinTuc tinTuc = list.get(position);

        holder.tvTenBaiBao.setText(tinTuc.getTenBaiBao());
        holder.tvDate.setText(tinTuc.getNgayDang());
        Picasso.get()
                .load(tinTuc.getAnh())
                .into(holder.img);
        holder.tvLuotXem.setText(tinTuc.getSoLuotXem()+ " lượt xem");

        holder.itemView.setOnClickListener(view -> itemCallback.onItemClick("IDBaiBao:" + String.valueOf(tinTuc.getIDBaiBao())));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTenBaiBao, tvDate, tvLuotXem;
        LinearLayout wrapperC;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenBaiBao = itemView.findViewById(R.id.tv_title_item_news);
            tvDate = itemView.findViewById(R.id.tv_date_news);
            img = itemView.findViewById(R.id.iv_news);
            wrapperC = itemView.findViewById(R.id.wrapper_news_item);
            tvLuotXem = itemView.findViewById(R.id.tv_luotXem);
        }
    }

}