package com.freedom.freeclient.freeclient.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.freedom.freeclient.freeclient.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class Util {
    static URL url;
    static String inputLine;

    public static void writeToFile(String content, String filePath, boolean append) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(
                        new File(filePath)));

        if(append){
            out.append(content);
        }else{
            out.write(content);
        }
        out.flush();
        out.close();
    }
    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        try {

            File f = new File(filename);

            if(!new File(f.getParent()).exists()){
                new File(f.getParent()).mkdir();
            }

            if(!f.exists()){
                writeToFile("",filename,false);
            }

            properties.load(new FileInputStream(f));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveProperties(Properties props, String filename) throws IOException, FileNotFoundException {
        props.store(new FileOutputStream(new File(filename)),"");
    }
    public static String  returnIp() {
        try {
            URL url = new URL("http://curlmyip.com/");
            // get URL content
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(url.openStream()));


            inputLine = br.readLine();
            //ipText.setText(inputLine);
            Log.d("DEBUG_MSG",inputLine);


            br.close();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputLine;

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
