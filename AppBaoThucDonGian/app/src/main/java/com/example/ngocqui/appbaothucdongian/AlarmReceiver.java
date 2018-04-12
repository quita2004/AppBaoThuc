package com.example.ngocqui.appbaothucdongian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("aaa", "xin chao");
        Intent myItent = new Intent(context, Music.class);

        String chuoi = intent.getExtras().getString("extra");
        Log.d("aaaa", chuoi);

        myItent.putExtra("extra", chuoi);
        context.startService(myItent);

    }
}
