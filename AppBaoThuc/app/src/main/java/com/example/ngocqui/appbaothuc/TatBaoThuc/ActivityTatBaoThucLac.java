package com.example.ngocqui.appbaothuc.TatBaoThuc;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.Music;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;

public class ActivityTatBaoThucLac extends AppCompatActivity implements SensorEventListener {

    Databases databases;

    SensorManager sensorManager;
    Sensor camBienGiaToc;
    float lastX, lastY, lastZ;
    long lastTime = 0;

    int soLanLac = 0;
    int idLoaiBaoThuc = 1;
    int id;

    int SO_LAN_PHAI_LAC = 10;
    int LOAI_LAC = 3000;

    TextView txtSoLanLac;
    TextView txtTatLacTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_bao_thuc_lac);

        txtSoLanLac = findViewById(R.id.textViewSoLanLac);
        txtTatLacTitle = findViewById(R.id.textViewTatLacTitle);

        Intent intent = getIntent();
        idLoaiBaoThuc = intent.getIntExtra("idLoaiBaoThuc", 123);
        id = intent.getIntExtra("id", 0);

        //lay thong tin so lan lac va do nhay
        databases = new Databases(this, "baothuc.sqlite", null, 1);
        Cursor dataLac = databases.getData("select * from LoaiLac WHERE Id = " + idLoaiBaoThuc);

        while (dataLac.moveToNext()){
            SO_LAN_PHAI_LAC = dataLac.getInt(1);
            int doNhay = dataLac.getInt(2);
            if (doNhay == 1){
                LOAI_LAC = 3000;
            } else if (doNhay == 2 ){
                LOAI_LAC = 4000;
            } else if (doNhay == 3){
                LOAI_LAC = 5000;
            }

        }

        txtSoLanLac.setText(soLanLac + " lần/" + SO_LAN_PHAI_LAC + " lần");
        txtTatLacTitle.setText("Bạn phải  lắc đủ " +  SO_LAN_PHAI_LAC + " lần để tắt báo thức");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        camBienGiaToc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (camBienGiaToc == null ){
            Toast.makeText(this, "Thiet bi ko ho tro cam bien", Toast.LENGTH_SHORT).show();
        }else{
            sensorManager.registerListener(this, camBienGiaToc, SensorManager.SENSOR_DELAY_FASTEST);
        }

    }
    public void TatBaoThuc(){

        checkBaoThucLAp();

        Intent intentAlarmReceiver = new Intent(ActivityTatBaoThucLac.this, Music.class);
        intentAlarmReceiver.putExtra("extra", "off");
        startService(intentAlarmReceiver);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        camBienGiaToc = event.sensor;
        if (camBienGiaToc.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastTime) > 100){
                long diffTime = currentTime - lastTime;
                lastTime = currentTime;

                float speed = Math.abs((x + y + z - lastX - lastY - lastZ)/diffTime * 10000);

                if (speed > LOAI_LAC){

                    Log.d("aaa", speed+"");
                    soLanLac++;
                    txtSoLanLac.setText(soLanLac + " lan/" + SO_LAN_PHAI_LAC + " lan");

                    if (soLanLac == SO_LAN_PHAI_LAC){
                        TatBaoThuc();

                        Intent mainIntent = new Intent(this, MainActivity.class);
                        startActivity(mainIntent);
                    }
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void checkBaoThucLAp() {
        Log.d("aaa", "id "+id);

        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Calendar calendar = Calendar.getInstance();

            Cursor dataNgayLap = databases.getData("select * from NgayLap where id = " + id);
            if (dataNgayLap.getCount() == 0){
                if (id != 0){
                    databases = new Databases(ActivityTatBaoThucLac.this, "baothuc.sqlite", null, 1);
                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 0 WHERE id =" + id);

                }
            }else {
                databases.QueryData("UPDATE BaoThuc SET IsTurn = 1 WHERE id =" + id);
                String thoiGian = "";
                Cursor dataBaoThuc = databases.getData("select * from BaoThuc where id = " + id);
                final Intent intent = new Intent(ActivityTatBaoThucLac.this, AlarmReceiver.class);

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
                        ActivityTatBaoThucLac.this, (id + 1) * 10000 + thu, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 7*24*60*60*1000, pendingIntent);
            }
        }
    }
}
