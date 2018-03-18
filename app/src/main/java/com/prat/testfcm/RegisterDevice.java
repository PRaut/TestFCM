package com.prat.testfcm;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterDevice extends AppCompatActivity {
    private EditText edtMobNo;
    String app_server_url = "http://petrographic-skirts.000webhostapp.com/insert_m.php";

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);
        edtMobNo = (EditText) findViewById(R.id.edtMobNo);

        builder = new AlertDialog.Builder(RegisterDevice.this);
    }


    // Register device
    @TargetApi(Build.VERSION_CODES.M)
    public void registerDevice(View v) {
        // Create sharedPref/ table to store Mob. no & ICCID
        // get mobile no & ICCID
        // store in pref and make flag to registered


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

        final String regisMobileNo = edtMobNo.getText().toString();

        String iccid = getICCID();
        if (iccid == null) {
            Toast.makeText(getApplicationContext(), "No SIM card detected", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putBoolean(Constants.REGISTERED, true);
        sEditor.putString(Constants.KEY_REG_MOBNO, regisMobileNo);
        sEditor.putString(Constants.KEY_ICCID, iccid);

        sEditor.apply();
        sEditor.commit();
        Log.i("DATA FOUND ", "###################" + iccid + "\t" + regisMobileNo + "###################");

        Toast.makeText(getApplicationContext(), "DATA saved " + regisMobileNo + "\n" + iccid, Toast.LENGTH_LONG).show();

        // ########### get fcm token #############
        SharedPreferences tokenPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), MODE_PRIVATE);
        final String token = tokenPreferences.getString(getString(R.string.FCM_TOKEN), "");

        Log.i("TOKEN DATA FOUND ", "###################" + token + "###################");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        Toast.makeText(getApplicationContext(), "Success " + token, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", "in error response \n" + error);
                Toast.makeText(RegisterDevice.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reg_mobno", regisMobileNo);
                params.put("fcm_token", token);
                Log.i("##### in Map ##### ", regisMobileNo + "\n" + token);
                return params;
            }
        };
        MySingleton.getmInstance(RegisterDevice.this).addToRequestQueue(stringRequest);


        goOnLoginWithFingerprintActivity();
    }

    private void goOnLoginWithFingerprintActivity() {
        startActivity(new Intent(this, LoginWithFingerprintActivity.class));
        this.finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private String getICCID() {
        // it takes permission READ_PHONE_STATE
       /* String simCountry = null;
        String simOperatorCode= null;
        String simOperatorName = null;
        */
        String simSerial = null;

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();

        switch (simState) {

            case (TelephonyManager.SIM_STATE_ABSENT):
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                break;
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
