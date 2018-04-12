package com.example.ngocqui.sqlitetodolist;

import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Databases databases;

    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = findViewById(R.id.listViewCV);
        arrayCongViec = new ArrayList<>();
        btnThem = findViewById(R.id.buttonAThem);

        adapter = new CongViecAdapter(this, R.layout.dong_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        databases = new Databases(this, "ghichu.sqlite", null, 1);

        databases.QueryData("CREATE TABLE IF NOT EXISTS CongViec( Id INTEGER PRIMARY KEY AUTOINCREMENT , TenCV VARCHAR(200))");


        //databases.QueryData("INSERT INTO CongViec VALUES(null, 'Viet ung dung android')");

        GetDataCV();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogThem();
            }
        });
    }

    private void DialogThem(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dong_cong_viec);

        final EditText edtTen = dialog.findViewById(R.id.editTextTenCVD);
        Button btnThem = dialog.findViewById(R.id.buttonThem);
        Button btnHuy = dialog.findViewById(R.id.buttonHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tencv = edtTen.getText().toString();
                if (tencv.equals("")){
                    Toast.makeText(MainActivity.this, "Nhap ten cv", Toast.LENGTH_SHORT).show();
                } else {
                    databases.QueryData("INSERT INTO CongViec VALUES(null, '" + tencv +"')");
                    Toast.makeText(MainActivity.this, "Da them", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCV();
                }
            }
        });

        dialog.show();
    }

    private  void GetDataCV(){
        Cursor dataCongViec = databases.getData("select * from CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }
}
