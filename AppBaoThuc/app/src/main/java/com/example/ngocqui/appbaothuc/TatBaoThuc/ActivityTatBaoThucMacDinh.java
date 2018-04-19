package com.example.ngocqui.appbaothuc.TatBaoThuc;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.EditBaoThuc.ActivityEditBaoThuc;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.Music;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;

public class ActivityTatBaoThucMacDinh extends Activity {

    Button btnTatmacDinh;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_bao_thuc_mac_dinh);

        btnTatmacDinh = findViewById(R.id.buttonTatBaothucMacDinh);

        final Intent getIntent = getIntent();
        id = getIntent.getIntExtra("id", 0);



        btnTatmacDinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBaoThucLAp();

                Log.d("ccc", "id "+id);

                Intent intentAlarmReceiver = new Intent(ActivityTatBaoThucMacDinh.this, Music.class);
                intentAlarmReceiver.putExtra("extra", "off");
                startService(intentAlarmReceiver);

                Intent mainInten = new Intent(ActivityTatBaoThucMacDinh.this, MainActivity.class);
                startActivity(mainInten);
            }
        });
    }

    private void checkBaoThucLAp() {
        Log.d("aaa", "id "+id);

        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Calendar calendar = Calendar.getInstance();

            Cursor dataNgayLap = databases.getData("select * from NgayLap where id = " + id);
            if (dataNgayLap.getCount() == 0){
                if (id != 0){
                    databases = new Databases(ActivityTatBaoThucMacDinh.this, "baothuc.sqlite", null, 1);
                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 0 WHERE id =" + id);

                }
            }else {
                databases.QueryData("UPDATE BaoThuc SET IsTurn = 1 WHERE id =" + id);
                String thoiGian = "";
                Cursor dataBaoThuc = databases.getData("select * from BaoThuc where id = " + id);
                final Intent intent = new Intent(ActivityTatBaoThucMacDinh.this, AlarmReceiver.class);

                while (dataBaoThuc.moveToNext()){
                    thoiGian = dataBaoThuc.getString(1);
                }
                int hour = Integer.parseInt(thoiGian.substring(0, 2));
                int mi = Integer.parseInt(thoiGian.substring(3, 5));

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, mi);
                calendar.set(Calendar.SECOND, 0);

                int thu = calendar.get(Calendar.DAY_OF_WEEK);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        ActivityTatBaoThucMacDinh.this, (id + 1) * 10000 + thu, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 7*24*60*60*1000, pendingIntent);
            }
        }
    }
}
