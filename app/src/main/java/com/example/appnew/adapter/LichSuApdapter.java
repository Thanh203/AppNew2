package com.example.appnew.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.Objects;

public class LichSuApdapter extends RecyclerView.Adapter<LichSuApdapter.myViewHolder>{
    Context context;
    List<TinTuc> list;
    ItemCallback itemCallback;
    String link;


    public LichSuApdapter(List<TinTuc> list, Context context, String link, ItemCallback itemCallback) {
        this.context = context;
        this.list = list;
        this.link = link;
        this.itemCallback = itemCallback;
    }

    public LichSuApdapter(Context context, ArrayList<TinTuc> list, String link) {
        this.context = context;
        this.list = list;
        this.link = link;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_lichsu, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        TinTuc tinTuc = list.get(position);

        holder.tvTenBaiBao.setText(tinTuc.getTenBaiBao());
        holder.tvDate.setText(tinTuc.getNgayDang());
        Picasso.get()
                .load(tinTuc.getAnh())
                .into(holder.img);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("History", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String id = sharedPreferences.getString("ID", "");
                ArrayList<String> idTinTuc = new ArrayList<>();
                String[] listID = id.split("/");

                for (String value : listID) {
                    if (value != null){
                        idTinTuc.add(value);
                    }
                }
                for (int i = 1; i < idTinTuc.size(); i++){
                    if (Objects.equals(tinTuc.getIDBaiBao(), idTinTuc.get(i))){
                        idTinTuc.set(i,"");
                    }
                }
                String idTT = "";
                for (int i = 1; i < idTinTuc.size(); i++){
                    if (idTinTuc.get(i) != null){
                        idTT = idTT + "/" + idTinTuc.get(i) ;
                    }
                }

                editor.putString("ID", idTT);
                editor.apply();

            }
        });


        holder.itemView.setOnClickListener(view -> itemCallback.onItemClick("IDBaiBao:" + String.valueOf(tinTuc.getIDBaiBao())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTenBaiBao, tvDate;
        LinearLayout wrapperC;
        ImageButton btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenBaiBao = itemView.findViewById(R.id.tv_title_item_news);
            tvDate = itemView.findViewById(R.id.tv_date_news);
            img = itemView.findViewById(R.id.iv_news);
            wrapperC = itemView.findViewById(R.id.wrapper_news_item);
            btnDelete = itemView.findViewById(R.id.ib_delete);
        }
    }

}
