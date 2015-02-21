package com.freedom.freeclient.freeclient;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class MainActivity extends ActionBarActivity {
    //String FILENAME = "hello_file";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startApp();
    }

    public void startApp(){
        Config.initStorage(this);
        final Context context = this;

        final Button button = (Button) findViewById(R.id.BtnStart);
        // get the user information from connectionActivity


        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent connectIntent = new Intent(context, ConnectActivity.class);
                startActivity(connectIntent);
            }
        });
    }





}
