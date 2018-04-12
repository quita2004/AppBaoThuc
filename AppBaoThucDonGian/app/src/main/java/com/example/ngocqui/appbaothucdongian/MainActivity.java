package com.example.ngocqui.appbaothucdongian;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnDungLai, btnHenGio;
    TextView txtHienThi;
    TimePicker timePicker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHenGio = findViewById(R.id.btnHenGIo);
        btnDungLai = findViewById(R.id.btnDungLai);
        txtHienThi = findViewById(R.id.txtHienThi);
        timePicker = findViewById(R.id.timePicker1);
        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);


        btnHenGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                String stringGio = String.valueOf(gio);
                String stringPhut = String.valueOf(phut);

                intent.putExtra("extra", "on");

                pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                txtHienThi.setText("Gio ban dat la: " + stringGio + ":" + stringPhut);

            }
        });

        btnDungLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHienThi.setText("Dung lai!");
                alarmManager.cancel(pendingIntent);
                intent.putExtra("extra", "off");
                sendBroadcast(intent);
            }
        });

    }
}
