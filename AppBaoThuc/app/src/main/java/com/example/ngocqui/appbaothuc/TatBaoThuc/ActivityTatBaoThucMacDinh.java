package com.example.ngocqui.appbaothuc.TatBaoThuc;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.Music;
import com.example.ngocqui.appbaothuc.R;

public class ActivityTatBaoThucMacDinh extends Activity {

    Button btnTatmacDinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_bao_thuc_mac_dinh);

        btnTatmacDinh = findViewById(R.id.buttonTatBaothucMacDinh);

        btnTatmacDinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = getIntent();
                int id = getIntent.getIntExtra("id", 0);

                if (id != 0){
                    Databases databases = new Databases(ActivityTatBaoThucMacDinh.this, "baothuc.sqlite", null, 1);
                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 0 WHERE id =" + id);
                    Log.d("aaa", "tat bao thuc "+id);
                }
                Log.d("ccc", "id "+id);

                Intent intentAlarmReceiver = new Intent(ActivityTatBaoThucMacDinh.this, Music.class);
                intentAlarmReceiver.putExtra("extra", "off");
                startService(intentAlarmReceiver);
//                finish();

                Intent mainInten = new Intent(ActivityTatBaoThucMacDinh.this, MainActivity.class);
                startActivity(mainInten);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("bbb", "mac dinh destroy");
    }
}
