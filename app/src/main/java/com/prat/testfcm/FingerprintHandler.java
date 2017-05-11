package com.prat.testfcm;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 26/03/2017.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler  extends FingerprintManager.AuthenticationCallback{
    private Context context;
   // boolean checkAuthentic = false;

   // LoginWithFingerprintActivity l;
    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
       // l = new LoginWithFingerprintActivity(context);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
       // Toast.makeText(context, "IN STARTAUTH", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }



    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.txtError);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
            //checkAuthentic = success;

           // l.sendToDisplayOTP(this.context);
            context.startActivity(new Intent(context, DisplayOTP.class));
        }
    }
/*
    public boolean isCheckAuthentic(){
        return checkAuthentic;
    }
    */
}

