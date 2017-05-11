package com.prat.testfcm;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegisterDevice extends AppCompatActivity {
    private EditText edtMobNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);
        edtMobNo = (EditText) findViewById(R.id.edtMobNo);
    }


    // Register device
    @TargetApi(Build.VERSION_CODES.M)
    public void registerDevice(View v){
        // Create sharedPref/ table to store Mob. no & ICCID
        // get mobile no & ICCID
        // store in pref and make flag to registered



        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

        String regisMobileNo = edtMobNo.getText().toString();

        String iccid = getICCID();
        if(iccid == null){
            Toast.makeText(getApplicationContext(), "No SIM card detected", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putBoolean(Constants.REGISTERED ,true);
        sEditor.putString( Constants.KEY_REG_MOBNO , regisMobileNo);
        sEditor.putString(Constants.KEY_ICCID, iccid);

        sEditor.apply();
        sEditor.commit();
        Log.i("DATA FOUND ", "###################"+iccid +"\t"+ regisMobileNo+ "###################");

        Toast.makeText(getApplicationContext(),"DATA saved "+regisMobileNo + "\n"+ iccid , Toast.LENGTH_LONG).show();

        goOnLoginWithFingerprintActivity();
    }

    private void goOnLoginWithFingerprintActivity() {
        startActivity(new Intent(this , LoginWithFingerprintActivity.class));
        this.finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private String getICCID(){
        // it takes permission READ_PHONE_STATE
       /* String simCountry = null;
        String simOperatorCode= null;
        String simOperatorName = null;
        */
        String simSerial = null;

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();

        switch (simState) {

            case (TelephonyManager.SIM_STATE_ABSENT): break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED): break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_UNKNOWN): break;
            case (TelephonyManager.SIM_STATE_READY): {

                // Get the SIM country ISO code
                // simCountry = telephonyManager.getSimCountryIso();

                // Get the operator code of the active SIM (MCC + MNC)
                // simOperatorCode = telephonyManager.getSimOperator();

                // Get the name of the SIM operator
                // simOperatorName = telephonyManager.getSimOperatorName();

                // Get the SIM’s serial number
                 simSerial = telephonyManager.getSimSerialNumber();
            }
        }
    return simSerial;
    }
}
