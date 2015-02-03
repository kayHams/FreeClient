package com.freedom.freeclient.freeclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.Util;


import java.io.IOException;
import java.util.Properties;


public class ConnectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        final Properties props = Util.getProperties(Config.INFO_FILE_PATH);

       if(props.get("proxy") ==null || props.get("proxy").equals("ask") ||
                props.get("proxy").equals("") ){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
       }

        new Thread(new Runnable(){
            public void run(){
                Log.e("DEBUG_MSG", "GOT TO Start Iodine");
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("org.xapek.andiodine");
                startActivity(LaunchIntent);
                Log.e("DEBUG_MSG", "DONE!!!!!!!!");
            }
        }).start();

        ImageButton btn = (ImageButton) findViewById(R.id.ImgBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://130.127.24.128:9090/FIP101/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
    }
    public boolean connect(){
        new Thread(new Runnable() {
            public void run() {


                final Properties props = Util.getProperties(Config.INFO_FILE_PATH);

                String ip = Util.returnIp();
                int countryNum = ClientLocation.MapCodeToCountry(ClientLocation.CountryCode(ip));
                String country_code = (String) props.get("country");
                String serveProxy = (String) props.get("proxy");
                Log.e("DEBUG_MSG",country_code);
                Log.e("DEBUG_MSG",serveProxy);
                if (serveProxy == "yes") {
                    String written_ip = ip;
                    Log.e("DEBUG_MSG", written_ip);

                } else {
                    String written_ip = "0,0,0,0";
                    Log.e("DEBUG_MSG", written_ip);

                }

                //new FileAsync().execute(1);
                //startBrowser();
            }

        }).start();

        return false;
    }
    class FileAsync extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            PasswordlessSSHManager.sftp_A2RS();
            return null;

        }
    }

    class StartAndiodine extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("DEBUG_MSG", "GOT TO Browser ACTIVITY");
            new Thread(new Runnable(){
                public void run(){
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("org.xapek.andiodine");
                    startActivity(LaunchIntent);
                    Log.e("DEBUG_MSG", "DONE!!!!!!!!");
                }
            }).start();

            return null;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
