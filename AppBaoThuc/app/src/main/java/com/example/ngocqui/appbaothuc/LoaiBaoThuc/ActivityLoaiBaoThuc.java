package com.example.ngocqui.appbaothuc.LoaiBaoThuc;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.Database.LoaiLac;
import com.example.ngocqui.appbaothuc.EditBaoThuc.ActivityEditBaoThuc;
import com.example.ngocqui.appbaothuc.R;
import com.example.ngocqui.appbaothuc.TatBaoThuc.ActivityTatBaothucGiaiToan;

public class ActivityLoaiBaoThuc extends AppCompatActivity {

    LinearLayout lnLoaiMacDinh, lnLoaiLac, lnLoaiGiaiToan, lnLoaiQuetMa;
    Button btnXacNhan, btnHuy;

    int loaiBaoThuc = 0;
    int idLoaiBaoThuc = 0;
    int id = 0;

    final int REQUES_CODE_LOAI_LAC = 123;
    final String COLOR_BACGROUND_SELECT = "#a0ff42";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_bao_thuc);

        lnLoaiMacDinh = findViewById(R.id.LineearLayoutMacDinh);
        lnLoaiLac = findViewById(R.id.LineearLayoutLoaiLac);
        lnLoaiGiaiToan = findViewById(R.id.LineearLayoutLoaiLamToan);
        lnLoaiQuetMa = findViewById(R.id.LineearLayoutLoaiQR);
        btnXacNhan = findViewById(R.id.buttonLoaiBaoThucXacNhan);
        btnHuy = findViewById(R.id.buttonLoaiBaoThucHuy);

        checkInfo();

        GanMauBackground();

        lnLoaiMacDinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaiBaoThuc = 0;
                GanMauBackground();
            }
        });

        lnLoaiLac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loaiLacIntent = new Intent(ActivityLoaiBaoThuc.this, ActivityLoaiLac.class);
                if (loaiBaoThuc == 1){
                    loaiLacIntent.putExtra("Id", idLoaiBaoThuc);
                }

                startActivityForResult(loaiLacIntent, REQUES_CODE_LOAI_LAC);


            }
        });

        lnLoaiGiaiToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loaiLacIntent = new Intent(ActivityLoaiBaoThuc.this, ActivityLoaiGiaiToan.class);
                if (loaiBaoThuc == 2){
                    loaiLacIntent.putExtra("Id", idLoaiBaoThuc);
                }
                startActivityForResult(loaiLacIntent, REQUES_CODE_LOAI_LAC);


            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoaiBaoThuc.this, ActivityEditBaoThuc.class);
                Log.d("aaa", "trong loai bao thuc "+idLoaiBaoThuc);
                intent.putExtra("LoaiBaoThuc", loaiBaoThuc);
                intent.putExtra("IdLoaiBaoThuc", idLoaiBaoThuc);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUES_CODE_LOAI_LAC && resultCode == RESULT_OK && data != null){

            idLoaiBaoThuc = data.getIntExtra("id", 1);
            loaiBaoThuc = data.getIntExtra("LoaiBaoThuc", 1);
            GanMauBackground();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void GanMauBackground(){
        switch (loaiBaoThuc){
            case 0:
                lnLoaiMacDinh.getBackground().setColorFilter(Color.parseColor(COLOR_BACGROUND_SELECT), PorterDuff.Mode.DARKEN);
                lnLoaiLac.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiGiaiToan.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiQuetMa.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                break;
            case 1:
                lnLoaiLac.getBackground().setColorFilter(Color.parseColor(COLOR_BACGROUND_SELECT), PorterDuff.Mode.DARKEN);
                lnLoaiMacDinh.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiGiaiToan.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiQuetMa.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                break;
            case 2:
                lnLoaiGiaiToan.getBackground().setColorFilter(Color.parseColor(COLOR_BACGROUND_SELECT), PorterDuff.Mode.DARKEN);
                lnLoaiMacDinh.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiLac.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiQuetMa.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                break;
            case 3:
                lnLoaiQuetMa.getBackground().setColorFilter(Color.parseColor(COLOR_BACGROUND_SELECT), PorterDuff.Mode.DARKEN);
                lnLoaiMacDinh.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiLac.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                lnLoaiGiaiToan.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN);
                break;
        }
    }

    private void checkInfo(){
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Cursor dataBaoThuc = databases.getData("select * from BaoThuc WHERE Id = " + id);

            while (dataBaoThuc.moveToNext()){
                loaiBaoThuc = dataBaoThuc.getInt(2);
                idLoaiBaoThuc = dataBaoThuc.getInt(3);
            }


        }
    }
}

