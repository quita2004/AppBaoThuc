package com.example.ngocqui.appbaothuc.PhatBaoThuc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ngocqui.appbaothuc.LoaiBaoThuc.ActivityLoaiGiaiToan;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucLac;
import com.example.ngocqui.appbaothuc.R;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucMacDinh;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaothucGiaiToan;

public class Music extends Service {

    MediaPlayer mediaPlayer;
    int id = 1;
    String key = "";
    static  int loaiBaoThuc = 0;
    static int idLoaiBaoThuc = 1;
    static int idBaoThuc = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getExtras().getString("extra") != null) {
            key = intent.getExtras().getString("extra");
        }

        Log.e("aaa", key);

        if (key.equals("on")) {
            id = 1;
        } else {
            id = 0;
        }

        Log.d("aaa", "key " + key);


        if (id == 1) {

            if (mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this, R.raw.demo);
                mediaPlayer.start();
            }

            Intent tatIntent = new Intent(this, ActivityTatBaoThucMacDinh.class);
            idLoaiBaoThuc = intent.getIntExtra("IdLoaiBaoThuc", 0);
            loaiBaoThuc = intent.getIntExtra("LoaiBaoThuc", 0);
            idBaoThuc = intent.getIntExtra("id", 0);
            Log.d("aaa", "loai bao thuc " + loaiBaoThuc);


            if (loaiBaoThuc == 0) {
                tatIntent = new Intent(this, ActivityTatBaoThucMacDinh.class);
            } else if (loaiBaoThuc == 1) {
                tatIntent = new Intent(this, ActivityTatBaoThucLac.class);
            } else if (loaiBaoThuc == 2) {
                tatIntent = new Intent(this, ActivityTatBaothucGiaiToan.class);
            }
            Log.d("aaa", "id loai bao thuc " + idLoaiBaoThuc);
            tatIntent.putExtra("idLoaiBaoThuc", idLoaiBaoThuc);
            tatIntent.putExtra("id", idBaoThuc);
            tatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(tatIntent);

            Log.d("aaa", "goi lan dau " + idLoaiBaoThuc + " , " + idBaoThuc);
            id = 0;
        } else if (id == 0) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }


        }

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//        }

        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.putExtra("extra", "on");
        restartServiceTask.putExtra("IdLoaiBaoThuc", idLoaiBaoThuc);
        restartServiceTask.putExtra("LoaiBaoThuc", loaiBaoThuc);
        restartServiceTask.putExtra("id", idBaoThuc);

        Log.d("aaa", "goi lai " + idLoaiBaoThuc + " , " + idBaoThuc);

        restartServiceTask.setPackage(getPackageName());

        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), id, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
