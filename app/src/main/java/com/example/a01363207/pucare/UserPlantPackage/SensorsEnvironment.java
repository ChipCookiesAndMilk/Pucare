package com.example.a01363207.pucare.UserPlantPackage;

import android.app.Service;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a01363207.pucare.R;

public class SensorsEnvironment  extends AppCompatActivity implements SensorEventListener {
    private TextView temperature, humidity, light;
    private SensorManager sensorManager;
    private Sensor sensorLight, sensorHumidity, sensorTemperature;

    private int t = -1, h = -1, l = -1;
    AnimationDrawable adTemperature, adHumidity, adLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if required load interface
        loadInterface();
        // initialize the sensors needed
        initSensors();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // start animation
        adTemperature.start();
        adHumidity.start();
        adLight.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature.setText(String.format("%s", event.values[0]));
            Log.d("SENSORS_CLASS", "Cambio la temperatura: " + event.values[0] + "");
            t = 1;
        }
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText(String.format("%s", event.values[0]));

            Log.d("SENSORS_CLASS", "Cambio la humedad: " + event.values[0] + "");
            h = 1;
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText(String.format("%s", event.values[0]));
            Log.d("SENSORS_CLASS", "Cambio la luz: " + event.values[0] + "");
            l = 1;
        }
        //Log.d("SENSORS_CLASS", "onSensorChanged: Temp: " + t + " Humidity: " + humidity + " Light: " + l);

        if(t != -1 && h != -1 && l != -1){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adTemperature.stop();
        adHumidity.stop();
        adLight.stop();
        finish();
    }

    private void initSensors(){
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

        sensorLight         = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorHumidity      = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorTemperature   = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (sensorTemperature == null) {
            Toast.makeText(this, "Your device has no temperature sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (sensorHumidity == null) {
            Toast.makeText(this, "Your device has no humidity sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (sensorLight == null) {
            Toast.makeText(this, "Your device has no light sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Toast.makeText(this,"Sensors were initialized",Toast.LENGTH_SHORT).show();
        //Log.d("SensorsEnvironment", "Sensors were initialized");
    }

    private void loadInterface(){
        setContentView(R.layout.sensors_view);
        temperature = (TextView)findViewById(R.id.idTemperature);
        humidity    = (TextView)findViewById(R.id.idHumidity);
        light       = (TextView)findViewById(R.id.idLight);

        ImageView ivTemperature = (ImageView)findViewById(R.id.ivTemperature);
        ImageView ivHumidity = (ImageView)findViewById(R.id.ivHumidity);
        ImageView ivLight = (ImageView)findViewById(R.id.ivLight);

        ivTemperature.setBackgroundResource(R.drawable.animation_temperature);
        ivHumidity.setBackgroundResource(R.drawable.animation_humidity);
        ivLight.setBackgroundResource(R.drawable.animation_light);

        adTemperature   = (AnimationDrawable)ivTemperature.getBackground();
        adHumidity      = (AnimationDrawable)ivHumidity.getBackground();
        adLight         = (AnimationDrawable)ivLight.getBackground();
    }
}
