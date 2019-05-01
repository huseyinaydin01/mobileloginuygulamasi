package com.hesapmakinasi;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageHesapmakinasi;
    ImageView imageIsik;

    boolean isikAcik = false;
    boolean isikIzni = false;

    boolean isikParliyor = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageIsik = findViewById(R.id.imageIsik);
        imageHesapmakinasi = findViewById(R.id.imageHesapmakinasi);


        imageIsik.setBackgroundColor(Color.RED);

        imageIsik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isikIzni){

                    if (!isikAcik){
                        isikAcik = true;
                        imageIsik.setBackgroundColor(Color.GREEN);
                    }else {
                        isikAcik = false;
                        imageIsik.setBackgroundColor(Color.RED);
                    }

                }else {
                    ayarlarControl();
                }
            }
        });

        imageHesapmakinasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HesapMakinasiActivity.class));
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake() {
                if (isikAcik && !isikParliyor){
                    isikParliyor = true;
                    setScreenBrightness(200);
                }
            }
            @Override
            public void onStop() {
                if (isikAcik && isikParliyor){
                    isikParliyor = false;
                    setScreenBrightness(10);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        ayarlarControl();
    }

    public void ayarlarControl(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                isikIzni = true;
            }
            else {
                Toast.makeText(this, "Uygulamanın ışık kontrolu için izinverilmesi gerekmektedir!!!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setScreenBrightness(int brightnessValue){
        if(brightnessValue >= 0 && brightnessValue <= 255 && isikIzni){
            Settings.System.putInt(
                    this.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
        }
    }
}
