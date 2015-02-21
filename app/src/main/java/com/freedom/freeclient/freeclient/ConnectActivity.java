package com.freedom.freeclient.freeclient;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.ImageButton;

import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.Util;


import java.io.File;

import java.util.Properties;



public class ConnectActivity extends ActionBarActivity {
    File proxyFile = new File(Config.getStorageDir(this) + Config.sep + "proxy.txt");
    File clientFile = new File(Config.getSendFile(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        final ProgressDialog dialog = ProgressDialog.show(this, "Please wait..", "Connecting...", true);

        final Properties props = Util.getProperties(Config.getInfoFilePath(this));

        // check if the user has entered their setting information, if not, go to settingsActivity, if yes, connect to iodine
        if(props.get("country") == null || props.get("country").equals("") ){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else{
            Log.e("DEBUG_MSG", "DO nothing, just testing");
            //Log.e("DEBUG_MSG", Config.storageDir());

        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
                new FileAsync().execute(1);
            }

        });
        t.start();
    }

    public boolean connect(){
        final Context context = ConnectActivity.this;
        new Thread(new Runnable() {
            public void run() {
                final Properties props = Util.getProperties(Config.getInfoFilePath(context));
                String country_code = (String) props.get("country");

                String ip = Util.returnIp();
                //Log.e("DEBUG_MSG", ip);

                int c = ClientLocation.MapCodeToCountry(ClientLocation.CountryCode(ip,context));
                String countryNum = Integer.toString(c);
                //Log.e("DEBUG_MSG", countryNum);

                String written_ip = "0.0.0.0";
                Util.writeToClientFile(country_code,countryNum,written_ip, Config.my_speed, context);
            }

        }).start();


        return false;
    }
    class FileAsync extends AsyncTask<Integer, Void, Void> {

        protected Void doInBackground(Integer... params) {
            PasswordlessSSHManager.sftp_A2RS(ConnectActivity.this);
            startBrowser();
            return null;

        }
    }

    public void startBrowser(){

        String url =  Util.readFromProxyFile(proxyFile);
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

    }

}
