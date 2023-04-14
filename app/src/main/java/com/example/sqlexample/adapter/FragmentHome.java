package com.example.sqlexample.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlexample.CrudActivity;
import com.example.sqlexample.R;
import com.example.sqlexample.dal.SQLiteHelper;
import com.example.sqlexample.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private SQLiteHelper db;
    private TextView tong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rView);
        tong = view.findViewById(R.id.tvTongtien);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
//        Item item = new Item("Tien nha thang 4","Sinh hoat","1300000","13/04/2023");
//        db.addItem(item);
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = db.getByDate(format.format(d));
        adapter.setList(list);
        tong.setText("Tong tien: "+Tong(list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(new RecycleViewAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Item item = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), CrudActivity.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });
    }
    private int Tong(List<Item> list){
        int t = 0;
        for(Item i:list){
            t+= Integer.parseInt(i.getPrice());
        }
        return t;
    }

    @Override
    public void onResume() {
        super.onResume();
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = db.getByDate(format.format(d));
        adapter.setList(list);
        tong.setText("Tong tien: "+Tong(list));
    }
}
