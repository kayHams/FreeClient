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
    File proxyFile = new File(Config.getStorageDir() + Config.sep + "proxy.txt");
    File clientFile = new File(Config.getSendFile());
    ProgressDialog dialog;
    private static final int SETTINGS_REQUEST =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


        final Properties props = Util.getProperties(Config.getInfoFilePath());

        // check if the user has entered their setting information, if not, go to settingsActivity, if yes, connect to iodine
        if(props.get("country") == null || props.get("country").equals("") ){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(settingsIntent, SETTINGS_REQUEST);
        }else{
            dialog = ProgressDialog.show(this, "Please wait..", "Connecting...", true);
            Log.i("DEBUG_MSG", "DO nothing, just testing");
            new CreateConnection(props).execute(1);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SETTINGS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {//settings was saved successfully
                final Properties props = Util.getProperties(Config.getInfoFilePath());
                new CreateConnection(props).execute(1);
            }
        }
    }

    public boolean connect(Properties props){

        String country_code = (String) props.get("country");

        String ip = Util.returnIp();
        //Log.e("DEBUG_MSG", ip);

        int c = ClientLocation.MapCodeToCountry(ClientLocation.CountryCode(ip,this));
        String countryNum = Integer.toString(c);
        Log.i("DEBUG_MSG", countryNum);

        String written_ip = "0.0.0.0";
        Util.writeToClientFile(country_code,countryNum,written_ip, Config.my_speed);


        return false;
    }
    class CreateConnection extends AsyncTask<Integer, Void, Void> {
        private Properties props;
        CreateConnection(Properties props){
            this.props = props;
        }
        protected Void doInBackground(Integer... params) {
            connect(props);
            PasswordlessSSHManager.sftp_A2RS(ConnectActivity.this);
            startBrowser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.cancel();
            super.onPostExecute(aVoid);
        }
    }

    public void startBrowser(){

        String url =  Util.readFromProxyFile(proxyFile);

        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
