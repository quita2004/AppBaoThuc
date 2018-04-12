package com.example.ngocqui.appbaothuc.LoaiBaoThuc;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
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

public class ActivityLoaiLac extends AppCompatActivity {

    NumberPicker numberPicker;
    RadioGroup radioGroup;
    RadioButton radioButtonDe, radioButtonTrungBinh, radioButtonKho;
    Button btnLoaiLacXacNhan, btnLoaiLacHuy;
    TextView txtLoaiLac;

    int soLanLac = 30;
    int doNhay = 1;

    int id = 0;

    final int LOAI_LAC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_lac);

        numberPicker = findViewById(R.id.numberPickerLoaiLac);
        radioGroup = findViewById(R.id.radioGroupLoaiGiaiToan);
        radioButtonDe = findViewById(R.id.radioButtonDe);
        radioButtonKho = findViewById(R.id.radioButtonKho);
        radioButtonTrungBinh = findViewById(R.id.radioButtonTrungBinh);
        btnLoaiLacXacNhan = findViewById(R.id.buttonLoaiGiaiToanXacNhan);
        btnLoaiLacHuy = findViewById(R.id.buttonLoaiLacHuy);
        txtLoaiLac = findViewById(R.id.textViewLoaiGiaiToan);

        numberPicker.setMinValue(30);
        numberPicker.setMaxValue(200);
        numberPicker.setWrapSelectorWheel(true);

        radioButtonDe.setChecked(true);

        checkInfo();

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtLoaiLac.setText("Lắc điện thoại " + newVal + " lần");
                soLanLac = newVal;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonDe){
                    doNhay = 1;
                } else if (checkedId == R.id.radioButtonTrungBinh){
                    doNhay = 2;
                } else {
                    doNhay = 3;
                }
            }
        });

        btnLoaiLacXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("aaa", "loai lac "+ soLanLac);
                Databases databases = new Databases(ActivityLoaiLac.this, "baothuc.sqlite", null, 1);

//                databases.QueryData("DROP TABLE LoaiLac");
                databases.QueryData("CREATE TABLE IF NOT EXISTS LoaiLac( Id INTEGER PRIMARY KEY AUTOINCREMENT , SoLanTat INTEGER, DoNhay INTEGER)");
                databases.QueryData("INSERT INTO LoaiLac VALUES(null," + soLanLac + ", " + doNhay +")");

                Cursor dataCongViec = databases.getData("select * from LoaiLac ORDER BY Id DESC LIMIT 0,1");

                int id = 1234;
                while (dataCongViec.moveToNext()){
                    id = dataCongViec.getInt(0);
                }

                Intent resultintent = new Intent(ActivityLoaiLac.this, ActivityLoaiBaoThuc.class);
                Log.d("aaa", "trong loai lac "+id);
                resultintent.putExtra("id", id);
                resultintent.putExtra("LoaiBaoThuc", LOAI_LAC);

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

    private void checkInfo(){
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        if (id != 0){
            Databases databases = new Databases(this, "baothuc.sqlite", null, 1);
            Cursor dataLac = databases.getData("select * from LoaiLac WHERE Id = " + id);

            while (dataLac.moveToNext()){
                soLanLac = dataLac.getInt(1);
                doNhay = dataLac.getInt(2);

                numberPicker.setValue(soLanLac);
                switch (doNhay){
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
