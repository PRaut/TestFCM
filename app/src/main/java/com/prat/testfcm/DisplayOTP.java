package com.prat.testfcm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayOTP extends AppCompatActivity implements OnProgressBarListener {

    private TextView txtOtp;
    private NumberProgressBar numberProgressBar;
    private Timer timer;

    private final String insertGeneratedOTP_URL = "http://petrographic-skirts.000webhostapp.com/insert_otp.php";
    private final String deleteOTP_URL = "http://petrographic-skirts.000webhostapp.com/delete_otp.php";

   // SharedPreferences mypref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
     String mobno; // = mypref.getString(Constants.KEY_REG_MOBNO, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_otp);

        SharedPreferences mypref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        mobno = mypref.getString(Constants.KEY_REG_MOBNO, "");
        txtOtp = (TextView) findViewById(R.id.otp);




        numberProgressBar = (NumberProgressBar) findViewById(R.id.numprogressbar);
        //numberProgressBar.setProgress(15);
        numberProgressBar.setOnProgressBarListener(this);


        final String OTP = generateOTP();

        final ProgressDialog pd = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setMessage("Sync in progress");
        pd.setCancelable(false);

        if (OTP != null) {
            pd.show();
            /*
            Send OTP update request from here ..
            if response is OK then show OTP and progress bar
             */

            StringRequest insertOTP_request = new StringRequest(Request.Method.POST, insertGeneratedOTP_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("DISPLAY OTP success", response);
                            if (response != null) {
                                pd.dismiss();
                                showProgressBar(OTP);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("DISPLAY OTP error", error.toString());
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> generatedOTP = new HashMap<>();
                    generatedOTP.put("otp", OTP);
                    generatedOTP.put("mobno", mobno);
                    return generatedOTP;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            MySingleton.getmInstance(DisplayOTP.this).addToRequestQueue(insertOTP_request);

            //showProgressBar(OTP);


        }

        /*timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberProgressBar.incrementProgressBy(1);
                        txtOtp.setText(generateOTP());
                    }
                });
            }
        }, 0, 300);*/
    }

    private void showProgressBar(final String otp) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberProgressBar.incrementProgressBy(1);
                        txtOtp.setText((otp));
                    }
                });
            }
        }, 0, 300);

    }

    private String generateOTP() {

        SecureRandom sr = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            otp.append(sr.nextInt(5));
        }
        Log.i("OTP", String.valueOf(otp));
        return String.valueOf(otp);
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            numberProgressBar.setProgress(0);
            //Toast.makeText(getApplicationContext(), "Times Over", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Your 30 Sec's Over", Toast.LENGTH_LONG).show();
            // Intent toLoginWithFinger = new Intent(this,LoginWithFingerprintActivity.class);
            //  startActivity(toLoginWithFinger);
            //this.finish();
            final ProgressDialog pd = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setMessage("Sync in progress");
            pd.setCancelable(false);
            pd.show();

            StringRequest updateOTP_request = new StringRequest(Request.Method.POST, deleteOTP_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("DELETED OTP SUCCESS", response);
                            if (response != null) {
                                pd.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("DELETED OTP ERROR", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> deleteOTP_param = new HashMap<>();
                    deleteOTP_param.put("otp", null);
                    deleteOTP_param.put("mobno", mobno);
                    return deleteOTP_param;
                }
            };
            MySingleton.getmInstance(DisplayOTP.this).addToRequestQueue(updateOTP_request);
        }
    }

}
