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
    private static final HashMap<String, String> proxyAnswer = new HashMap<>();
    private static URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        proxyAnswer.put("Yes always", "yes");
        proxyAnswer.put("No always", "no");
        proxyAnswer.put("Always ask me", "ask");

        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Error","In initUI() "+e.getMessage());
        }
    }

    void initUI() throws Exception{
        //String []c = new String[]{"BENIN | B\\u00c9NIN","BURKINA FASO | BURKINA FASO","CAMEROON | CAMEROUN","CHAD | TCHAD","IVORY COAST | COTE D'IVOIRE","DJIBOUTI | DJIBOUTI","FRANCE | FRANCE","EQUATORIAL GUINEA | GUIN\\u00c9E \\u00c9QUATORIALE","GAMBIA | GAMBIE",
        //"GUINEA CONAKRY | GUIN\\u00c9E CONAKRY","LIBERIA | LIBERIA","NETHERLANDS | PAYS-BAS","NIGERIA | NIGERIA","NORWAY | NORV\\u00c9GE","SENEGAL | S\\u00c9N\\u00c9GAL","TOGO | TOGO","UGANDA | OUGANDA","GABON | GABON","CENTRAL AFRICAN REPUBLIC  | R\\u00c9PUBLIQUE CENTRAFRICAINE","US | US"};

        /*for(int i=1;i<c.length;i++){
            Country co = new Country();
            co.setName(c[i]);
            co.setId(""+i);
            countries.add(co);
        }*/

        CountriesParser parser = new CountriesParser();
        countries = parser.parse(this.getResources().openRawResource(R.raw.country_data));
        //init countries list
        Spinner spinner = (Spinner) findViewById(R.id.countryDropDown);
        ArrayAdapter<Country> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button cancel = (Button) this.findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
    public void onSaveSetting(View view) {

        Spinner spinner = (Spinner) this.findViewById(R.id.countryDropDown);
        Country country = (Country) spinner.getSelectedItem();
        RadioGroup gro = (RadioGroup) this.findViewById(R.id.radioGroup);
        RadioButton rdio = (RadioButton) this.findViewById(gro.getCheckedRadioButtonId());
        StringBuilder buf = new StringBuilder();

        final Properties props = Util.getProperties(Config.INFO_FILE_PATH);
        props.put("country", country.getId());
        props.put("proxy",proxyAnswer.get((String)rdio.getText()));
        new Thread(new Runnable(){
            public void run(){
                try {
                    Util.saveProperties(props, Config.INFO_FILE_PATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        finish();
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
