package com.example.myconputer.json_listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleContactActivity  extends Activity {

    // JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_CITY = "city";
    private static final String TAG_COUNTRY = "country";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangmoi);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String email = in.getStringExtra(TAG_CITY);
        String mobile = in.getStringExtra(TAG_COUNTRY);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.txtn);
        TextView lblEmail = (TextView) findViewById(R.id.txtm);
        TextView lblMobile = (TextView) findViewById(R.id.txtmob);

        lblName.setText(name);
        lblEmail.setText(email);
        lblMobile.setText(mobile);
    }
}