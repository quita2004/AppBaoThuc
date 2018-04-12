package com.example.ngocqui.appbaothuc;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.EditBaoThuc.ActivityEditBaoThuc;
import com.example.ngocqui.appbaothuc.Database.BaoThuc;
import com.example.ngocqui.appbaothuc.Database.BaoThucAdapter;
import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucLac;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucMacDinh;

import java.sql.Time;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    static int active = 0;

    Databases databases;

    ListView lvBaoThuc;
    ArrayList<BaoThuc> arrayBaoThuc;
    BaoThucAdapter adapter;

    Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        active = 1;

        lvBaoThuc = findViewById(R.id.listViewBT);
        arrayBaoThuc = new ArrayList<>();



        btnThem = findViewById(R.id.buttonMainThem);

        adapter = new BaoThucAdapter(this, R.layout.dong_main_bao_thuc, arrayBaoThuc);
        lvBaoThuc.setAdapter(adapter);

        //ham tao databases
        TaoDatabases();

        //lay du lieu
        GetDataBT();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityEditBaoThuc.class);
                startActivity(intent);
            }
        });


        lvBaoThuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "main", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GetDataBT();
    }

    private void TaoDatabases(){
        databases = new Databases(this, "baothuc.sqlite", null, 1);
//        databases.QueryData("DROP TABLE BaoThuc");
        databases.QueryData(
                "CREATE TABLE IF NOT EXISTS BaoThuc( Id INTEGER PRIMARY KEY AUTOINCREMENT , ThoiGian TIME, LoaiBaoThuc INTEGER, IdLoaiBaoThuc INTEGER, IsTurn INTEGER)");

//        databases.QueryData("INSERT INTO CongViec VALUES(null, 1, " + thoiGian + ")");
    }

    private  void GetDataBT(){
        Cursor dataCongViec = databases.getData("select * from BaoThuc");
        arrayBaoThuc.clear();
        while (dataCongViec.moveToNext()){
            int id = dataCongViec.getInt(0);
            int cachtat = dataCongViec.getInt(2);
            String thoiGian = dataCongViec.getString(1);
            int isBat = dataCongViec.getInt(4);

            arrayBaoThuc.add(new BaoThuc(id, cachtat, thoiGian, isBat));
        }
        adapter.notifyDataSetChanged();
    }

}
