package com.example.ngocqui.appbaothucdongian;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class Music extends Service {

    MediaPlayer mediaPlayer;
    int id = 1;
    String key = "";
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

        Log.e("Music nhan key", key);

        if (key.equals("on")){
            id = 1;
        } else{
            id = 0;
        }
        if (id == 1){
            mediaPlayer = MediaPlayer.create(this, R.raw.demo);
            mediaPlayer.start();
            id = 0;
        } else if (id == 0){
            if (mediaPlayer != null)
            mediaPlayer.pause();
            Log.d("bbb", "Da tat  ");
        }

        Log.d("bbb", "Trong music " + id);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
