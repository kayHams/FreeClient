package com.freedom.freeclient.freeclient;

import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.CountriesParser;
import com.freedom.freeclient.freeclient.util.Util;


import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Properties;



public class SettingsActivity extends ActionBarActivity {
    private static ArrayList<Country> countries= new ArrayList();
    private static URL url;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initUI() throws Exception{

        CountriesParser parser = new CountriesParser();
        countries = parser.parse(this.getResources().openRawResource(R.raw.country_data));

        Spinner spinner = (Spinner) findViewById(R.id.countryDropDown);
        ArrayAdapter<Country> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button cancel = (Button) this.findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(context, MainActivity.class);
                startActivity(MainIntent);
                SettingsActivity.this.finish();
            }
        });
    }
    public void onSaveSetting(View view) {

        Spinner spinner = (Spinner) this.findViewById(R.id.countryDropDown);
        Country country = (Country) spinner.getSelectedItem();
        StringBuilder buf = new StringBuilder();

        final Properties props = Util.getProperties(Config.getInfoFilePath());
        props.put("country", country.getId());

        new SaveAsync(props).execute();

    }
    class SaveAsync extends AsyncTask<Void, Void, Void> {
        private Properties props;
        SaveAsync(Properties props){
            this.props = props;
        }
        protected Void doInBackground(Void... params) {
            try {
                Util.saveProperties(props, Config.getInfoFilePath(),SettingsActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
