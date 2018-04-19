package com.example.ngocqui.appbaothuc.TatBaoThuc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.LoaiBaoThuc.ActivityLoaiGiaiToan;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.Music;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;
import java.util.Random;

public class ActivityTatBaothucGiaiToan extends AppCompatActivity {

    Databases databases;

    TextView txtTatGiaiToanTong, txtTatGiaiToanTraLoi, txtTatGiaiToanCauHoi;
    EditText edtTatGiaiToanTraLoi;
    Button btnTatGiaiToanTraLoi;

    int tongSoPhepTinh = 3;
    int doKho = 1;
    int phepTinhLamDuoc = 0;
    int id;

    int idLoaiBaoThuc = 1;

    int a = 0, b = 0, c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_baothuc_giai_toan);

        txtTatGiaiToanCauHoi = findViewById(R.id.textViewTatGiaiToanCauHoi);
        txtTatGiaiToanTong = findViewById(R.id.textViewTatGiaiToanTong);
        txtTatGiaiToanTraLoi = findViewById(R.id.textViewTatGiaiToanTraLoi);
        btnTatGiaiToanTraLoi = findViewById(R.id.buttonTatGiaiToanTraLoi);
        edtTatGiaiToanTraLoi = findViewById(R.id.editTextTatGiaiToanTraLoi);

        Intent intent = getIntent();
        idLoaiBaoThuc = intent.getIntExtra("idLoaiBaoThuc", 123);
        id = intent.getIntExtra("id", 0);

        //lay thong tin so lan lac va do nhay
        databases = new Databases(this, "baothuc.sqlite", null, 1);
        Cursor dataLac = databases.getData("select * from LoaiGiaiToan WHERE Id = " + idLoaiBaoThuc);
        Log.d("aaa", "trong tat "+idLoaiBaoThuc);
        while (dataLac.moveToNext()){
            tongSoPhepTinh = dataLac.getInt(1);
            doKho = dataLac.getInt(2);
            Log.d("aaa", "do kho tat "+doKho);
        }

        txtTatGiaiToanTong.setText("Trả lời đúng " + tongSoPhepTinh + " câu để tắt báo thức");
        txtTatGiaiToanTraLoi.setText("Bạn đã trả lời đúng " + phepTinhLamDuoc + "/" + tongSoPhepTinh + " câu hỏi");
        TaoCauHoi(doKho);

        btnTatGiaiToanTraLoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dapan = a + b + c;
                int cauTraLoi = Integer.parseInt(edtTatGiaiToanTraLoi.getText().toString());

                if (dapan == cauTraLoi ){
                    phepTinhLamDuoc++;
                    Toast.makeText(ActivityTatBaothucGiaiToan.this, "Chính xác", Toast.LENGTH_SHORT).show();
                    txtTatGiaiToanTraLoi.setText("Bạn đã trả lời đúng " + phepTinhLamDuoc + "/" + tongSoPhepTinh + " câu hỏi");
                    if (phepTinhLamDuoc == tongSoPhepTinh){
                        TatBaoThuc();
                    } else{
                        TaoCauHoi(doKho);
                    }
                } else{
                    TaoCauHoi(doKho);
                    Toast.makeText(ActivityTatBaothucGiaiToan.this, "Sai rồi", Toast.LENGTH_SHORT).show();
                }
                edtTatGiaiToanTraLoi.setText("");

            }
        });
    }

    public void TatBaoThuc(){
        Intent intentAlarmReceiver = new Intent(ActivityTatBaothucGiaiToan.this, Music.class);
        intentAlarmReceiver.putExtra("extra", "off");
        startService(intentAlarmReceiver);

        checkBaoThucLAp();
        Intent mainInten = new Intent(ActivityTatBaothucGiaiToan.this, MainActivity.class);
        startActivity(mainInten);
    }

    private void TaoCauHoi(int doKho){
         int so = (int) Math.pow(10, doKho+1);

         Random r = new Random();
         a = r.nextInt(so - (int) Math.pow(10, doKho)) + (int) Math.pow(10, doKho);
         b = r.nextInt(so - (int) Math.pow(10, doKho)) + (int) Math.pow(10, doKho);
         c = r.nextInt(so - (int) Math.pow(10, doKho)) + (int) Math.pow(10, doKho);
         txtTatGiaiToanCauHoi.setText(a + "+" + b + "+" + c + "=?");
    }

    private void checkBaoThucLAp() {
        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Calendar calendar = Calendar.getInstance();

            Cursor dataNgayLap = databases.getData("select * from NgayLap where id = " + id);
            if (dataNgayLap.getCount() == 0){
                if (id != 0){
                    databases = new Databases(ActivityTatBaothucGiaiToan.this, "baothuc.sqlite", null, 1);
                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 0 WHERE id =" + id);

                }
            }else {
                databases.QueryData("UPDATE BaoThuc SET IsTurn = 1 WHERE id =" + id);
                String thoiGian = "";
                Cursor dataBaoThuc = databases.getData("select * from BaoThuc where id = " + id);
                final Intent intent = new Intent(ActivityTatBaothucGiaiToan.this, AlarmReceiver.class);

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
                        ActivityTatBaothucGiaiToan.this, (id + 1) * 10000 + thu, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 7*24*60*60*1000, pendingIntent);
            }
        }
    }
}
