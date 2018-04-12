package com.example.ngocqui.appbaothuc.LoaiBaoThuc;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ngocqui.appbaothuc.Database.Databases;
import com.example.ngocqui.appbaothuc.R;

public class ActivityLoaiGiaiToan extends Activity {

    TextView txtLoaiGiaiToan, txtLoaiGiaiToanMuc;
    NumberPicker numberPicker;
    RadioGroup radioGroup;
    RadioButton radioButtonDe, radioButtonTrungBinh, radioButtonKho;
    Button btnLoaiLacXacNhan, btnLoaiLacHuy;

    int soPhepTinh = 3;
    int doKho = 1;

    int id = 0;

    final int LOAI_GIAI_TOAN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_giai_toan);

        txtLoaiGiaiToan = findViewById(R.id.textViewLoaiGiaiToan);
        txtLoaiGiaiToanMuc = findViewById(R.id.textViewLoaiGiaiToanMuc);
        radioGroup = findViewById(R.id.radioGroupLoaiGiaiToan);
        radioButtonDe = findViewById(R.id.radioButtonGiaiToanDe);
        radioButtonTrungBinh = findViewById(R.id.radioButtonGiaiToanTrungBinh);
        radioButtonKho = findViewById(R.id.radioButtonGiaiToanKho);
        btnLoaiLacXacNhan = findViewById(R.id.buttonLoaiGiaiToanXacNhan);
        btnLoaiLacHuy = findViewById(R.id.buttonLoaiLacHuy);
        numberPicker = findViewById(R.id.numberPickerLoaiGiaiToan);

        numberPicker.setMinValue(3);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(true);

        radioButtonDe.setChecked(true);

        checkInfo();

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                soPhepTinh = newVal;
                txtLoaiGiaiToan.setText("Giải " + soPhepTinh + " bài toán");
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonGiaiToanDe){
                    doKho = 1;
                    txtLoaiGiaiToanMuc.setText("13+34+65=?");
                } else if (checkedId == R.id.radioButtonGiaiToanTrungBinh){
                    doKho = 2;
                    txtLoaiGiaiToanMuc.setText("135+642+814=?");
                } else if (checkedId == R.id.radioButtonGiaiToanKho){
                    doKho = 3;
                    txtLoaiGiaiToanMuc.setText("1248+9627+4320=?");
                }
                Log.d("aaa", "do kho radio "+ doKho);
            }
        });

        btnLoaiLacXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Databases databases = new Databases(ActivityLoaiGiaiToan.this, "baothuc.sqlite", null, 1);

//                databases.QueryData("DROP TABLE LoaiGiaiToan");
                databases.QueryData("CREATE TABLE IF NOT EXISTS LoaiGiaiToan( Id INTEGER PRIMARY KEY AUTOINCREMENT , SoPhepTinh INTEGER, DoKho INTEGER)");
                databases.QueryData("INSERT INTO LoaiGiaiToan VALUES(null," + soPhepTinh + ", " + doKho +")");

                Log.d("aaa", "do kho "+doKho);

                Cursor dataCongViec = databases.getData("select * from LoaiGiaiToan ORDER BY Id DESC LIMIT 0,1");

                int id = 1234;
                while (dataCongViec.moveToNext()){
                    id = dataCongViec.getInt(0);
                }

                Intent resultintent = new Intent(ActivityLoaiGiaiToan.this, ActivityLoaiBaoThuc.class);
                Log.d("aaa", "trong loai giai toan "+id);
                resultintent.putExtra("id", id);
                resultintent.putExtra("LoaiBaoThuc", LOAI_GIAI_TOAN);

                setResult(RESULT_OK, resultintent);
                finish();
            }
        });

        btnLoaiLacHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkInfo() {
        Intent intent = getIntent();
        id = intent.getIntExtra("idLoaiBaoThuc", 0);
        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Cursor data = databases.getData("select * from LoaiGiaiToan WHERE Id = " + id);

            while (data.moveToNext()){
                soPhepTinh = data.getInt(1);
                doKho = data.getInt(2);

                numberPicker.setValue(soPhepTinh);
                switch (doKho){
                    case 1:
                        radioButtonDe.setChecked(true);
                        break;
                    case 2:
                        radioButtonTrungBinh.setChecked(true);
                        break;
                    case 3:
                        radioButtonKho.setChecked(true);
                        break;
                }
            }
        }
    }
}
