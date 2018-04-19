package com.example.ngocqui.appbaothuc.EditBaoThuc;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.LoaiBaoThuc.ActivityLoaiBaoThuc;
import com.example.ngocqui.appbaothuc.MainActivity;
import com.example.ngocqui.appbaothuc.PhatBaoThuc.AlarmReceiver;
import com.example.ngocqui.appbaothuc.R;

import java.util.Calendar;

public class ActivityEditBaoThuc extends AppCompatActivity {

    Databases databases;

    ConstraintLayout constraintLayoutLoaiTat, constraintLayoutLapLai;
    TextView txtLoaiBaoThuc, txtLapLai;
    FrangmentEditTimepicker frangmentEditTimepicker;
    Button btnEditThem, btnEditHuy, btnEditXoa;

    CheckBox cbThuHai, cbThuBa, cbThuTu, cbThuNam, cbThuSau, cbThuBay, cbChuNhat;
    Button btnNgayLapXacNhan, btnNgayLapHuy;

    TimePicker timePicker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Dialog dialog;

    final int REQUEST_CODE = 345;

    int loaiBaoThuc = 0;
    int idLoaiBaoThuc = 1;
    int id = 0;

    int[] ngayLap = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bao_thuc);

        btnEditThem = findViewById(R.id.buttonEditThem);
        btnEditHuy = findViewById(R.id.buttonEditHuy);
        btnEditXoa = findViewById(R.id.buttonEditXoa);

        constraintLayoutLoaiTat = findViewById(R.id.constraintLayoutLoaiTat);
        constraintLayoutLapLai = findViewById(R.id.ConstraintLayoutLapLai);

        txtLoaiBaoThuc = constraintLayoutLoaiTat.findViewById(R.id.textViewEditLoaiTat);
        frangmentEditTimepicker = (FrangmentEditTimepicker) getFragmentManager().findFragmentById(R.id.fragmentEditTimepicker);
        timePicker = frangmentEditTimepicker.timePicker;

        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent intent = new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class);

        //dialog ngay lap
        dialog = new Dialog(ActivityEditBaoThuc.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ngay_lap);
        dialog.setCanceledOnTouchOutside(false);
        cbThuHai = dialog.findViewById(R.id.checkBoxThuHai);
        cbThuBa = dialog.findViewById(R.id.checkBoxThuBa);
        cbThuTu = dialog.findViewById(R.id.checkBoxThuTu);
        cbThuNam = dialog.findViewById(R.id.checkBoxThuNam);
        cbThuSau = dialog.findViewById(R.id.checkBoxThuSau);
        cbThuBay = dialog.findViewById(R.id.checkBoxThuBay);
        cbChuNhat = dialog.findViewById(R.id.checkBoxChuNhat);
        txtLapLai = constraintLayoutLapLai.findViewById(R.id.textViewLaplai);

        btnNgayLapXacNhan = dialog.findViewById(R.id.buttonNgayLapXacNhan);
        btnNgayLapHuy = dialog.findViewById(R.id.buttonNgayLapHuy);


        checkInfo();
        checkNgayLap();

        constraintLayoutLoaiTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ActivityEditBaoThuc.this, ActivityLoaiBaoThuc.class);
                mainIntent.putExtra("Id", id);
                startActivityForResult(mainIntent, REQUEST_CODE);
            }
        });

        constraintLayoutLapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ngayLap.length == 7) {
                    for (int i = 0; i < ngayLap.length; i++) {
                        if (ngayLap[i] > 0) {
                            setCheckedNgayLap(ngayLap[i]);
                        }
                    }
                }
                btnNgayLapXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = 0;
                        String textNgaylap = "Không lặp";
                        if (cbChuNhat.isChecked() || cbThuBay.isChecked() || cbThuSau.isChecked() || cbThuNam.isChecked()
                                || cbThuTu.isChecked() || cbThuBa.isChecked() || cbThuHai.isChecked()) {
                            ngayLap = new int[7];
                            textNgaylap = "";
                            if (cbThuHai.isChecked()) {
                                ngayLap[i] = 2;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbThuBa.isChecked()) {
                                ngayLap[i] = 3;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbThuTu.isChecked()) {
                                ngayLap[i] = 4;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbThuNam.isChecked()) {
                                ngayLap[i] = 5;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbThuSau.isChecked()) {
                                ngayLap[i] = 6;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbThuBay.isChecked()) {
                                ngayLap[i] = 7;
                                textNgaylap += "Th " + ngayLap[i] + ". ";
                                i++;
                            }
                            if (cbChuNhat.isChecked()) {
                                ngayLap[i] = 1;
                                textNgaylap += "CN.";
                            }

                            txtLapLai.setText(textNgaylap);

                        } else {
                            txtLapLai.setText("Không lặp");
                            ngayLap = new int[1];
                        }
                        dialog.dismiss();
                    }
                });

                btnNgayLapHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btnEditThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);

                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                String stringGio = String.valueOf(gio);
                String stringPhut = String.valueOf(phut);
                if (phut < 10) {
                    stringPhut = "0" + stringPhut;
                }

                String thoiGian = stringGio + ":" + stringPhut;
                databases = new Databases(ActivityEditBaoThuc.this, "baothuc.sqlite", null, 1);

                if (id == 0) {
                    databases.QueryData("INSERT INTO BaoThuc VALUES(null, '" + thoiGian + "', " + loaiBaoThuc + ", " + idLoaiBaoThuc + ", 1)");
                    Cursor data = databases.getData("select * from BaoThuc ORDER BY Id DESC LIMIT 0,1");
                    while (data.moveToNext()) {
                        id = data.getInt(0);
                    }
                } else {
                    databases.QueryData("UPDATE BaoThuc SET ThoiGian = '" + thoiGian + "', LoaiBaoThuc = " +
                            loaiBaoThuc + ", IdLoaiBaoThuc = " + idLoaiBaoThuc + ", IsTurn = 1 WHERE Id = " + id);
                }

                intent.putExtra("extra", "on");
                intent.putExtra("LoaiBaoThuc", loaiBaoThuc);
                intent.putExtra("IdLoaiBaoThuc", idLoaiBaoThuc);
                intent.putExtra("id", id);


                //dat bao thuc
                if (ngayLap.length == 1) {
//                    Calendar cld = Calendar.getInstance();
//
//                    int date = calendar.get(Calendar.DAY_OF_YEAR);
//                    if (calendar.getTimeInMillis() < cld.getTimeInMillis()) {
//                        calendar.set(Calendar.DATE, date + 1);
//                    }

                    Cursor dataNgayLap = databases.getData("select * from NgayLap where IdBaoThuc = " + id);
                    while (dataNgayLap.moveToNext()) {

                        int ngayLap = dataNgayLap.getInt(2);
                        PendingIntent alarmIntent;
                        alarmIntent = PendingIntent.getBroadcast(ActivityEditBaoThuc.this, (id + 1) * 10000 + ngayLap,
                                new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class),
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmIntent.cancel();
                    }
                    databases.QueryData("DELETE FROM NgayLap WHERE IdBaoThuc = " + id);
                    pendingIntent = PendingIntent.getBroadcast(
                            ActivityEditBaoThuc.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else if (ngayLap.length == 7) {
                    Log.d("aaa", "dat bao thuc lap");
//                    databases.QueryData("DROP TABLE Ngaylap");
                    databases.QueryData("CREATE TABLE IF NOT EXISTS Ngaylap( Id INTEGER PRIMARY KEY AUTOINCREMENT , IdBaoThuc INTEGER, NgayLap INTEGER)");
                    if (id > 0) {
                        databases.QueryData("DELETE FROM NgayLap WHERE IdBaoThuc = " + id);
                    }

                    for (int i = 0; i < ngayLap.length; i++) {
                        if (ngayLap[i] != 0) {
                            //xoa tat ca bao thuc lap cu
                            if (id > 0){
                                PendingIntent alarmIntent;
                                alarmIntent = PendingIntent.getBroadcast(ActivityEditBaoThuc.this, (id + 1) * 10000 + ngayLap[i],
                                        new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class),
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                alarmIntent.cancel();
                            }


                            int cong = 0;


                            databases.QueryData("INSERT INTO NgayLap VALUES(null, " + id + ", " + ngayLap[i] + ")");

                            calendar.set(Calendar.DAY_OF_WEEK, ngayLap[i]);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                                cong = 1;
                            }

                            pendingIntent = PendingIntent.getBroadcast(
                                    ActivityEditBaoThuc.this, (id + 1) * 10000 + ngayLap[i], intent, PendingIntent.FLAG_UPDATE_CURRENT
                            );
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Log.d("aaa", "id bao thuc " + id);
                            Log.d("aaa", "them lap ngay " + ngayLap[i]);

                            if (cong == 1) {
                                calendar.add(Calendar.DAY_OF_YEAR, -7);
                            }
                        }
                    }
                }

                Toast.makeText(ActivityEditBaoThuc.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                finish();
            }
        });


        btnEditHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (id == 0) {
            btnEditXoa.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            loaiBaoThuc = data.getIntExtra("LoaiBaoThuc", 1);
            idLoaiBaoThuc = data.getIntExtra("IdLoaiBaoThuc", 1233);
            Log.d("aaa", "result edit " + idLoaiBaoThuc);
            switch (loaiBaoThuc) {
                case 0:
                    txtLoaiBaoThuc.setText("Mặc định");
                    break;
                case 1:
                    txtLoaiBaoThuc.setText("Lắc điện thoại");
                    break;
                case 2:
                    txtLoaiBaoThuc.setText("Giải toán");
                    break;
                case 3:
                    txtLoaiBaoThuc.setText("Quét mã");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkInfo() {
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        Log.d("aaa", "id = " + id);

        if (id != 0) {
            databases = new Databases(this, "baothuc.sqlite", null, 1);
            Cursor dataBaoThuc = databases.getData("select * from BaoThuc WHERE Id = " + id);

            String thoiGian = "";
            while (dataBaoThuc.moveToNext()) {
                loaiBaoThuc = dataBaoThuc.getInt(2);
                thoiGian = dataBaoThuc.getString(1);
            }
            int hour = Integer.parseInt(thoiGian.substring(0, 2));
            int mi = Integer.parseInt(thoiGian.substring(3, 5));

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(mi);
            switch (loaiBaoThuc) {
                case 0:
                    txtLoaiBaoThuc.setText("Mặc định");
                    break;
                case 1:
                    txtLoaiBaoThuc.setText("Lắc điện thoại");
                    break;
                case 2:
                    txtLoaiBaoThuc.setText("Giải toán");
                    break;
                case 3:
                    txtLoaiBaoThuc.setText("Quét mã");
                    break;
            }

            btnEditXoa.setVisibility(View.VISIBLE);
            btnEditXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databases.QueryData("DELETE FROM BaoThuc WHERE id = " + id);
                    databases.QueryData("DELETE FROM NgayLap WHERE IdBaoThuc = " + id);
                    Cursor dataBaoThuc = databases.getData("select IsTurn from BaoThuc where id = " + id);
                    int dangBat = 0;
                    while (dataBaoThuc.moveToNext()){
                        dangBat = dataBaoThuc.getInt(0);
                    }


                    if (dangBat == 1){
                        Cursor dataNgayLap = databases.getData("select * from NgayLap where IdBaoThuc = " + id);
                        if (dataNgayLap.getCount() == 0) {
                            PendingIntent alarmIntent;
                            alarmIntent = PendingIntent.getBroadcast(ActivityEditBaoThuc.this, id,
                                    new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class),
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            alarmIntent.cancel();
                        } else {
                            while (dataNgayLap.moveToNext()) {
                                int ngayLap = dataNgayLap.getInt(2);
                                PendingIntent alarmIntent;
                                alarmIntent = PendingIntent.getBroadcast(ActivityEditBaoThuc.this, (id + 1) * 10000 + ngayLap,
                                        new Intent(ActivityEditBaoThuc.this, AlarmReceiver.class),
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                alarmIntent.cancel();
                            }
                        }
                    }


                    Intent intent = new Intent(ActivityEditBaoThuc.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }


    }

    private void checkNgayLap() {
        if (id != 0) {
            int i = 0;
            Cursor dataNgayLap = databases.getData("select * from NgayLap where IdBaoThuc = " + id);
            if (dataNgayLap.getCount() > 0) {
                ngayLap = new int[7];
            }
            while (dataNgayLap.moveToNext()) {
                if (i == 7) {
                    break;
                }
                ngayLap[i] = dataNgayLap.getInt(2);
                i++;
            }
            String textNgaylap = "";

            for (int j = 0; j < dataNgayLap.getCount(); j++) {
                setCheckedNgayLap(ngayLap[j]);
                if (ngayLap[j] != 1) {
                    textNgaylap += "Th " + ngayLap[j] + ". ";
                } else {
                    textNgaylap += "CN" + ". ";
                }

            }
            if (dataNgayLap.getCount() > 0) {
                txtLapLai.setText(textNgaylap);
            } else {
                txtLapLai.setText("Không lặp");
            }
        }
    }

    private void setCheckedNgayLap(int ngayLap) {
        switch (ngayLap) {
            case 2:
                cbThuHai.setChecked(true);
                break;
            case 3:
                cbThuBa.setChecked(true);
                break;
            case 4:
                cbThuTu.setChecked(true);
                break;
            case 5:
                cbThuNam.setChecked(true);
                break;
            case 6:
                cbThuSau.setChecked(true);
                break;
            case 7:
                cbThuBay.setChecked(true);
                break;
            case 1:
                cbChuNhat.setChecked(true);
                break;

        }
    }
}
