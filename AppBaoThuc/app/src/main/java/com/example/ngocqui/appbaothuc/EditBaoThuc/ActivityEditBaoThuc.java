package com.example.ngocqui.appbaothuc.EditBaoThuc;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.LoaiBaoThuc.ActivityLoaiBaoThuc;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;

public class ActivityEditBaoThuc extends AppCompatActivity {

    Databases databases;

    ConstraintLayout constraintLayoutLoaiTat;
    TextView txtLoaiBaoThuc;
    FrangmentEditTimepicker frangmentEditTimepicker;
    Button btnEditThem, btnEditHuy, btnEditXoa;

    TimePicker timePicker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    final int REQUEST_CODE = 345;

    int loaiBaoThuc = 0;
    int idLoaiBaoThuc = 1;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bao_thuc);

        btnEditThem = findViewById(R.id.buttonEditThem);
        btnEditHuy = findViewById(R.id.buttonEditHuy);
        btnEditXoa = findViewById(R.id.buttonEditXoa);

        constraintLayoutLoaiTat = findViewById(R.id.constraintLayoutLoaiTat);

        txtLoaiBaoThuc = constraintLayoutLoaiTat.findViewById(R.id.textViewEditLoaiTat);
        frangmentEditTimepicker = (FrangmentEditTimepicker) getFragmentManager().findFragmentById(R.id.fragmentEditTimepicker);
        timePicker = frangmentEditTimepicker.timePicker;

        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent intent = new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class);

        checkInfo();

        constraintLayoutLoaiTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ActivityEditBaoThuc.this, ActivityLoaiBaoThuc.class);
                mainIntent.putExtra("Id", id);
                startActivityForResult(mainIntent, REQUEST_CODE);
            }
        });

        btnEditThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                String stringGio = String.valueOf(gio);
                String stringPhut = String.valueOf(phut);
                if (phut < 10){
                    stringPhut = "0" + stringPhut;
                }

                String thoiGian =  stringGio + ":" + stringPhut;
                databases = new Databases(ActivityEditBaoThuc.this, "baothuc.sqlite", null, 1);

                if (id == 0){
                    databases.QueryData("INSERT INTO BaoThuc VALUES(null, '" + thoiGian + "', " + loaiBaoThuc + ", "  + idLoaiBaoThuc + ", 1)");
                    Cursor data = databases.getData("select * from BaoThuc ORDER BY Id DESC LIMIT 0,1");
                    while (data.moveToNext()){
                        id = data.getInt(0);
                    }
                } else{
                    databases.QueryData("UPDATE BaoThuc SET ThoiGian = '" + thoiGian + "', LoaiBaoThuc = "  +
                            loaiBaoThuc + ", IdLoaiBaoThuc = " + idLoaiBaoThuc + ", IsTurn = 1 WHERE Id = " + id);
                }

                intent.putExtra("extra", "on");
                intent.putExtra("LoaiBaoThuc", loaiBaoThuc);
                intent.putExtra("IdLoaiBaoThuc", idLoaiBaoThuc);
                intent.putExtra("id", id);

                Calendar cld = Calendar.getInstance();

                int date = calendar.get(Calendar.DATE);
//                if (calendar.getTimeInMillis() < cld.getTimeInMillis()){
//                    calendar.set(Calendar.DATE, date+1);
//                }

                pendingIntent = PendingIntent.getBroadcast(
                        ActivityEditBaoThuc.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                Toast.makeText(ActivityEditBaoThuc.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        btnEditHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (id == 0){
            btnEditXoa.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            loaiBaoThuc = data.getIntExtra("LoaiBaoThuc", 1);
            idLoaiBaoThuc = data.getIntExtra("IdLoaiBaoThuc", 1233);
            Log.d("aaa", "result edit "+idLoaiBaoThuc);
            switch (loaiBaoThuc){
                case 0:
                    txtLoaiBaoThuc.setText("Mặc định");
                    break;
                case 1:
                    txtLoaiBaoThuc.setText("Lắc điện thoại");
                    break;
                case 2:
                    txtLoaiBaoThuc.setText("Giải toán");
                    break;
                case 3:
                    txtLoaiBaoThuc.setText("Quét mã");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkInfo(){
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        Log.d("aaa", "id = "+id);

        if (id != 0){
            databases = new Databases(this, "baothuc.sqlite", null, 1);
            Cursor dataBaoThuc = databases.getData("select * from BaoThuc WHERE Id = " + id);

            String thoiGian = "";
            while (dataBaoThuc.moveToNext()){
                loaiBaoThuc = dataBaoThuc.getInt(2);
                thoiGian = dataBaoThuc.getString(1);
            }
            int hour = Integer.parseInt(thoiGian.substring(0,2));
            int mi = Integer.parseInt(thoiGian.substring(3,5));

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(mi);
            switch (loaiBaoThuc){
                case 0:
                    txtLoaiBaoThuc.setText("Mặc định");
                    break;
                case 1:
                    txtLoaiBaoThuc.setText("Lắc điện thoại");
                    break;
                case 2:
                    txtLoaiBaoThuc.setText("Giải toán");
                    break;
                case 3:
                    txtLoaiBaoThuc.setText("Quét mã");
                    break;
            }

            btnEditXoa.setVisibility(View.VISIBLE);
            btnEditXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databases.QueryData("DELETE FROM BaoThuc WHERE id = " + id);

                    Intent intent = new Intent(ActivityEditBaoThuc.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }


    }
}
