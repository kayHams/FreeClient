package com.freedom.freeclient.freeclient.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.freedom.freeclient.freeclient.MainActivity;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class Util {
    static URL url;
    static String inputLine;

    public static void writeToClientFile(String real_country, String chosen_country, String written_ip, String speed){
        File f = new File(Config.getSendFile());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            f.createNewFile();

            out.append(real_country + "+");
            out.append(chosen_country + "+");
            out.append(written_ip + "+");
            out.append(speed);
            out.close();
            fos.close();
        } catch (FileNotFoundException e ){
            e.printStackTrace();
        }catch( IOException ex) {
            ex.printStackTrace();
        }

    }

    public static String readFromProxyFile(File proxy){
        String proxyUrl="";
        try{
            FileReader fr = new FileReader(proxy);
            BufferedReader br = new BufferedReader(fr);

            proxyUrl = br.readLine();
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return proxyUrl;
    }

    public static void writeToFile(String content, String filePath, boolean append) throws IOException {
        FileOutputStream fos =  new FileOutputStream(new File(filePath));
        OutputStreamWriter out = new OutputStreamWriter(fos);

        if(append){
            out.append(content);
        }else{
            out.write(content);
        }
        out.flush();
        out.close();
    }

    public static void writeToFile(InputStream is, String outFile) throws IOException{
        FileUtils.copyInputStreamToFile(is, new File(outFile));
    }

    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        try {

            File f = new File(filename);

            if(!f.exists()){
                writeToFile("",filename,false);
            }

            properties.load(new FileInputStream(f));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveProperties(Properties props, String filename, Context context) throws IOException, FileNotFoundException {
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
}
