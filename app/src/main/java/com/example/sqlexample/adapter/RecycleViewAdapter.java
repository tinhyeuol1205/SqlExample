package com.example.sqlexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlexample.R;
import com.example.sqlexample.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.HomeViewHoder>{
    private List<Item> mList;
    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public RecycleViewAdapter() {
        this.mList = new ArrayList<>();
    }

    public void setList(List<Item> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    @Override
    public HomeViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new HomeViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHoder holder, int position) {
        Item item = mList.get(position);
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class HomeViewHoder extends RecyclerView.ViewHolder{
        private TextView title,category,price,date;
        public HomeViewHoder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        itemListener.onItemClick(itemView,getAdapterPosition());
                    }
                }
            });
        }
    }
    public interface ItemListener{
        void onItemClick(View view,int position);
    }
}
