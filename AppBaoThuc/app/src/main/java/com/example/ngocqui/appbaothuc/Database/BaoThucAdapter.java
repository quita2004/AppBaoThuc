package com.example.ngocqui.appbaothuc.Database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.EditBaoThuc.ActivityEditBaoThuc;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class BaoThucAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<BaoThuc> baoThucList;

    int id ;

    public BaoThucAdapter(Context context, int layout, List<BaoThuc> baoThucList) {
        this.context = context;
        this.layout = layout;
        this.baoThucList = baoThucList;
    }

    @Override
    public int getCount() {
        return baoThucList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtGio, txtNgayLap;
        CheckBox cbBat;
        ImageView imgLoaiBaoThuc;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final BaoThuc baoThuc;
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtGio = convertView.findViewById(R.id.textViewGioBaoThuc);
            holder.cbBat = convertView.findViewById(R.id.checkBoxBaoThuc);
            holder.imgLoaiBaoThuc = convertView.findViewById(R.id.imageViewLoaiBaoThuc);
            holder.txtNgayLap = convertView.findViewById(R.id.textViewDongBaoThucNgayLap);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        baoThuc = baoThucList.get(position);
        id = baoThuc.getId();
        holder.txtGio.setText(baoThuc.getThoiGian()+"");
        switch (baoThuc.getCachTat()){
            case 0:
                holder.imgLoaiBaoThuc.setImageResource(R.drawable.alarm);
                break;
            case 1:
                holder.imgLoaiBaoThuc.setImageResource(R.drawable.shakephone);
                break;
            case 2:
                holder.imgLoaiBaoThuc.setImageResource(R.drawable.math);
                break;
            case 3:
                holder.imgLoaiBaoThuc.setImageResource(R.drawable.qrcode);
                break;
        }

        if (baoThuc.getIsBat() == 1){
            holder.cbBat.setChecked(true);
        } else{
            holder.cbBat.setChecked(false);
        }


        checkNgayLap(holder.txtNgayLap );

        //bat su kien sua
        holder.cbBat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);;
                PendingIntent pendingIntent = null;
                Databases databases = new Databases(context, "baothuc.sqlite", null, 1);
                Cursor dataNgayLap = databases.getData("select * from NgayLap where IdBaoThuc = " + id);

                if ( holder.cbBat.isChecked()){
                    String thoiGian = baoThuc.getThoiGian();
                    int hour = Integer.parseInt(thoiGian.substring(0,2));
                    int mi = Integer.parseInt(thoiGian.substring(3,5));

                    Calendar calendar = Calendar.getInstance();
                    int date = calendar.get(Calendar.DATE);
                    final Intent intent = new Intent(context, AlarmReceiver.class);

                    intent.putExtra("id", id);
                    intent.putExtra("extra", "on");

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, mi);

//                    Calendar cld = Calendar.getInstance();
//
////                    if (calendar.getTimeInMillis() < cld.getTimeInMillis()){
////                        calendar.set(Calendar.DATE, date+1);
////                    }


                    if (dataNgayLap.getCount() == 0){
                        pendingIntent = PendingIntent.getBroadcast(
                                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT
                        );

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    } else {
                        while (dataNgayLap.moveToNext()){
                            int ngayLap = dataNgayLap.getInt(2);

                            int cong = 0;

                            calendar.set(Calendar.DAY_OF_WEEK, ngayLap);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                                cong = 1;
                            }

                            pendingIntent = PendingIntent.getBroadcast(
                                    context, (id + 1) * 10000 + ngayLap, intent, PendingIntent.FLAG_UPDATE_CURRENT
                            );
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            if (cong == 1) {
                                calendar.add(Calendar.DAY_OF_YEAR, -7);
                            }
                        }
                    }

                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 1 WHERE id =" + id);
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else if ( !holder.cbBat.isChecked()){
                    if (dataNgayLap.getCount() == 0){
                        PendingIntent alarmIntent;
                        alarmIntent = PendingIntent.getBroadcast(context, id,
                                new Intent(context, AlarmReceiver.class),
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmIntent.cancel();
                    } else {
                        while (dataNgayLap.moveToNext()){
                            int ngayLap = dataNgayLap.getInt(2);
                            PendingIntent alarmIntent;
                            alarmIntent = PendingIntent.getBroadcast(context, (id + 1) * 10000 + ngayLap,
                                    new Intent(context, AlarmReceiver.class),
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            alarmIntent.cancel();
                        }
                    }

                    Toast.makeText(context, "Tắt báo thức", Toast.LENGTH_SHORT).show();

                    databases.QueryData("UPDATE BaoThuc SET IsTurn = 0 WHERE id =" + id);



                }

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityEditBaoThuc.class);
                intent.putExtra("Id", baoThuc.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    private void checkNgayLap(TextView tv){
        if (id != 0){
            Databases databases = new Databases(context, "baothuc.sqlite", null, 1);
            Cursor dataNgayLap = databases.getData("select * from NgayLap where IdBaoThuc = " + id);

            Log.d("ddd", "id = "+id);
            String textNgaylap = "";
            while (dataNgayLap.moveToNext()){
                int ngayLap = dataNgayLap.getInt(2);
                if (ngayLap > 1){
                    textNgaylap += "Th " + ngayLap + ". ";
                } else if (ngayLap == 1){
                    textNgaylap += "CN.";
                }
            }

            if (dataNgayLap.getCount() > 0){
                tv.setText(textNgaylap);
            } else {
                tv.setText("Không lặp");
            }
        }
    }
}
