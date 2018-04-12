package com.example.ngocqui.democambiengiatoc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager sensorManager;
    Sensor camBienGiaToc;
    float lastX = 0, lastY = 0, lastZ = 0;
    long lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        camBienGiaToc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (camBienGiaToc == null ){
            Toast.makeText(this, "Thiet bi ko ho tro cam bien", Toast.LENGTH_SHORT).show();
        }else{
            sensorManager.registerListener(this, camBienGiaToc, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        camBienGiaToc = event.sensor;
        if (camBienGiaToc.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastTime) > 100){
                long diffTime = currentTime - lastTime;
                lastTime = currentTime;

                float speed = Math.abs((x + y + z - lastX - lastY - lastZ)/diffTime * 10000);

                if (speed > 5000){
                    Toast.makeText(this, "Ban vua lac thiet bi " + speed, Toast.LENGTH_SHORT).show();
                    Log.d("aaa", speed+"");
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
