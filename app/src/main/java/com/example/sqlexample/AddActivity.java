package com.example.sqlexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlexample.dal.SQLiteHelper;
import com.example.sqlexample.model.Item;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText eTitle,ePrice,eDate;
    private Spinner sp;
    private Button btUpdate,btCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        eTitle = findViewById(R.id.eTitle);
        ePrice = findViewById(R.id.ePrice);
        eDate = findViewById(R.id.eDate);
        btUpdate = findViewById(R.id.btUpdate);
        btCancel = findViewById(R.id.btCancel);
        sp = findViewById(R.id.spinner);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = eTitle.getText().toString();
                String c = sp.getSelectedItem().toString();
                String p = ePrice.getText().toString();
                String d = eDate.getText().toString();
                if(!t.isEmpty()&&p.matches("\\d+")){
                    SQLiteHelper db = new SQLiteHelper(AddActivity.this);
                    Item item = new Item(t,c,p,d);
                    db.addItem(item);
                };
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}