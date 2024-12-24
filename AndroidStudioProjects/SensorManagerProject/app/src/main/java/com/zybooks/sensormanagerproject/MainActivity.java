package com.zybooks.sensormanagerproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView accelerometerValuesTextView;

    private SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // just playing around with the listener
                long timeStamp = event.timestamp;
                long timeStampSeconds = timeStamp / 1000000000;
                String dateString = String.valueOf(timeStampSeconds);

                String text = "X: " + x + "\nY: " + y + "\nZ: " + z + "\n" + "Timestamp seconds: " + dateString;
                accelerometerValuesTextView.setText(text);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // won't work without this
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometerValuesTextView = findViewById(R.id.accelerometerValues);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null) {
            accelerometerValuesTextView.setText("No Accelerometer Sensor Found");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometerListener);
    }

}