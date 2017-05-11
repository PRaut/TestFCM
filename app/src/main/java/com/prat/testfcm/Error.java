package com.prat.testfcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public  class Error extends AppCompatActivity {

    private TextView txtErrorMsg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        txtErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);

        Intent getError = getIntent();
        txtErrorMsg.setText(getError.getStringExtra("hardwareError"));
    }
}
