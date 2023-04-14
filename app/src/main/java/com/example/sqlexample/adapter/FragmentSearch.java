package com.example.sqlexample.adapter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlexample.R;
import com.example.sqlexample.dal.SQLiteHelper;
import com.example.sqlexample.model.Item;

import java.util.Calendar;
import java.util.List;

public class FragmentSearch extends Fragment {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private SQLiteHelper db;
    private TextView tong;
    private Spinner sp;
    private SearchView searchView;
    private Button btSearch;
    private EditText eFrom,eTo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.search);
        eFrom = view.findViewById(R.id.eFrom);
        eTo = view.findViewById(R.id.eTo);
        sp = view.findViewById(R.id.spCategory);
        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1 = new String[arr.length+1];
        arr1[0] = "All";
        for (int i = 0; i < arr.length; i++) {
            arr1[i+1] = arr[i];
        }
        sp.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.item_spinner,arr1));
        btSearch = view.findViewById(R.id.btSearch);
        tong = view.findViewById(R.id.tvTongtien);
        recyclerView = view.findViewById(R.id.rView);
        db = new SQLiteHelper(view.getContext());
        adapter = new RecycleViewAdapter();
        List<Item> list = db.getAll();
        tong.setText("Tổng tiền: "+tong(list));
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Item> list1 = db.searchByTitle(s);
                tong.setText("Tổng tiền: "+tong(list1));
                adapter.setList(list1);
                return true;
            }
        });
        eFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if (m > 8) {
                            date = d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                        eFrom.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        eTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if (m > 8) {
                            date = d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                        eTo.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = eFrom.getText().toString();
                String to = eTo.getText().toString();
                if(!from.isEmpty()&&!to.isEmpty()){
                    List<Item> list1 = db.getByDateFromTo(from,to);
                    adapter.setList(list1);
                    tong.setText("Tổng tiền: "+tong(list1));
                }
            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String c = sp.getItemAtPosition(i).toString();
                List<Item> list1;
                if(c.equalsIgnoreCase("all")){
                    list1 = db.getAll();
                }
                else {
                    list1 = db.searchByCategory(c);
                }
                adapter.setList(list1);
                tong.setText("Tổng tiền: "+tong(list1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int tong(List<Item> list) {
        int t = 0;
        for(Item i:list){
            t+= Integer.parseInt(i.getPrice());
        }
        return t;
    }
}
