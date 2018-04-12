package com.example.ngocqui.listviewnangcao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvTraiCay;
    ArrayList<TraiCay> arrayTraiCay;
    TraiCayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        adapter = new TraiCayAdapter(this, R.layout.dong_trai_cay, arrayTraiCay);
        lvTraiCay.setAdapter(adapter);



    }

    private void AnhXa(){
        lvTraiCay = findViewById(R.id.listViewTraiCay);
        arrayTraiCay = new ArrayList<>();

        arrayTraiCay.add(new TraiCay("A", "Mo ta 1", R.drawable.a));
        arrayTraiCay.add(new TraiCay("B", "Mo ta 2", R.drawable.b));
        arrayTraiCay.add(new TraiCay("C", "Mo ta 3", R.drawable.c));
        arrayTraiCay.add(new TraiCay("A", "Mo ta 1", R.drawable.a));
        arrayTraiCay.add(new TraiCay("B", "Mo ta 2", R.drawable.b));
        arrayTraiCay.add(new TraiCay("C", "Mo ta 3", R.drawable.c));
        arrayTraiCay.add(new TraiCay("A", "Mo ta 1", R.drawable.a));
        arrayTraiCay.add(new TraiCay("B", "Mo ta 2", R.drawable.b));
        arrayTraiCay.add(new TraiCay("C", "Mo ta 3", R.drawable.c));


    }
}
