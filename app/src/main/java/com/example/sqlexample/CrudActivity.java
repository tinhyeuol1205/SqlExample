package com.example.sqlexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqlexample.dal.SQLiteHelper;
import com.example.sqlexample.model.Item;

import java.util.Calendar;

public class CrudActivity extends AppCompatActivity {
    private EditText eTitle,ePrice,eDate;
    private Spinner sp;
    private Button btUpdate,btCancel,btDelete;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        initView();
    }

    private void initView() {
        eTitle = findViewById(R.id.eTitle);
        ePrice = findViewById(R.id.ePrice);
        eDate = findViewById(R.id.eDate);
        sp = findViewById(R.id.spinner);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);
        btCancel = findViewById(R.id.btCancel);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CrudActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if (m > 8) {
                            date = d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                        eDate.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        int p = 0;
        for(int i=0;i<sp.getCount();i++){
            if(item.getCategory().equalsIgnoreCase(sp.getItemAtPosition(i).toString())) {
                p = i;
                break;
            }
        }
        sp.setSelection(p);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = eTitle.getText().toString();
                String c = sp.getSelectedItem().toString();
                String p = ePrice.getText().toString();
                String d = eDate.getText().toString();
                if(!t.isEmpty()&&p.matches("\\d+")){
                    SQLiteHelper db = new SQLiteHelper(CrudActivity.this);
                    Item i = new Item(item.getId(),t,c,p,d);
                    db.updateItem(i);
                };
                Intent intent = new Intent(CrudActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteHelper db = new SQLiteHelper(CrudActivity.this);
//                String t = eTitle.getText().toString();
//                String c = sp.getSelectedItem().toString();
//                String p = ePrice.getText().toString();
//                String d = eDate.getText().toString();
//                if(t.equalsIgnoreCase(item.getTitle())
//                        &&c.equalsIgnoreCase(item.getCategory())
//                        &&p.equalsIgnoreCase(item.getPrice())
//                        &&d.equalsIgnoreCase(item.getDate())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Thông báo xóa");
                    builder.setMessage("Bạn có chắc chắn muốn xóa "+item.getTitle()+" không?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.deleteItem(item.getId());
                            finish();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
//                }
//                else {
//                    Toast.makeText(CrudActivity.this, "Không thể xóa", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}