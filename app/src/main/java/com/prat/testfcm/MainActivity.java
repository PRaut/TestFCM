package com.prat.testfcm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isRegistered()) {
                        // startService(new Intent(this, NotificationListener.class);
                        startActivity(new Intent(MainActivity.this, LoginWithFingerprintActivity.class));
                        finish();
                    } else {
                        //FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                        //Log.i("test", fingerprintManager.toString());
                        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(getApplicationContext());
                        /*if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            //return;
                            Toast.makeText(getApplicationContext(), "No Hardware Found", Toast.LENGTH_LONG).show();
                        }*/
                      if (!fingerprintManagerCompat.isHardwareDetected()) {
                           // Toast.makeText(getApplicationContext(), "Device Does not have Fingerprint Hardware ", Toast.LENGTH_LONG).show();
                          Log.i("no Hardware", "no hardware founds");
                          Intent noHardwareIntent = new Intent(MainActivity.this , Error.class);
                          noHardwareIntent.putExtra("hardwareError", "Device Does not have Fingerprint Hardware");
                          startActivity(noHardwareIntent);
                        } else {
                            startActivity(new Intent(MainActivity.this, RegisterDevice.class));
                            finish();
                        }
                    }
                }
            }
        }.start();

    }

    private boolean isRegistered() {
        //Getting shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

        //Getting the value from shared preferences
        //The second parameter is the default value
        //if there is no value in sharedprference then it will return false
        //that means the device is not registered
        return sharedPreferences.getBoolean(Constants.REGISTERED, false);
    }

}
