package com.example.ngocqui.appbaothuc.PhatBaoThuc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucLac;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaoThucMacDinh;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaothucGiaiToan;

public class AlarmReceiver extends BroadcastReceiver {

    int loaiBaoThuc = 0;
    int idLoaiBaoThuc = 1;
    int  id = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myItent = new Intent(context, Music.class);

        String chuoi = intent.getExtras().getString("extra");
        loaiBaoThuc = intent.getIntExtra("LoaiBaoThuc", 0);
        idLoaiBaoThuc = intent.getIntExtra("IdLoaiBaoThuc", 1);
        id = intent.getIntExtra("id", 1);

        myItent.putExtra("extra", chuoi);
        myItent.putExtra("IdLoaiBaoThuc", idLoaiBaoThuc);
        myItent.putExtra("LoaiBaoThuc", loaiBaoThuc);
        myItent.putExtra("id", id);

        Log.d("aaa", "chuoi "+ chuoi);

        context.startService(myItent);


    }
}
