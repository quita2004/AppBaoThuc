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
    int loaiBaoThuc = 0;
    int idLoaiBaoThuc = 1;
    int idBaoThuc = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras().getString("extra") != null){
            key = intent.getExtras().getString("extra");
        }

        Log.e("aaa", key);

        if (key.equals("on")){
            id = 1;
        } else{
            id = 0;
        }

        Log.d("aaa", "key " + key);

        if (id == 1){


            idLoaiBaoThuc = intent.getIntExtra("IdLoaiBaoThuc", 0);
            loaiBaoThuc = intent.getIntExtra("LoaiBaoThuc", 0);
            idBaoThuc = intent.getIntExtra("id", 0);
            Log.d("aaa", "loai bao thuc "+loaiBaoThuc);

            Intent tatIntent = new Intent(this, ActivityTatBaoThucMacDinh.class);
            if (loaiBaoThuc == 0){
                tatIntent = new Intent(this, ActivityTatBaoThucMacDinh.class);
            } else if (loaiBaoThuc == 1){
                tatIntent = new Intent(this, ActivityTatBaoThucLac.class);
            } else if (loaiBaoThuc == 2){
                tatIntent = new Intent(this, ActivityTatBaothucGiaiToan.class);
            }
            Log.d("aaa", "id loai bao thuc "+idLoaiBaoThuc);
            tatIntent.putExtra("idLoaiBaoThuc", idLoaiBaoThuc);
            tatIntent.putExtra("id", idBaoThuc);
            tatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(tatIntent);

            mediaPlayer = MediaPlayer.create(this, R.raw.demo);
            mediaPlayer.start();
            Log.d("aaa", "bat nhac");
            id = 0;
        } else if (id == 0){
            if (mediaPlayer != null)
                mediaPlayer.pause();
            Log.d("aaa", "Da tat  ");
        }

        return START_NOT_STICKY;
    }

}
