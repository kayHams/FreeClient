package com.freedom.freeclient.freeclient.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class Config {
    public static final String my_speed = "0";
    public static String sep = System.getProperty("file.separator");
    public static final String REAL_DOMAIN_FILE = "domain.txt";
    public static final String SSH_FILE_NAME = "client_info.txt";
    public static final String INFO_FILE_NAME = "option.props";
    public static final String SPEEED_FILE_NAME = "speed.txt";
    public static final String APP_DIR="freeclient";
    public static final String fileName = "id_rsa";
    public static final String domainName = "ifserver.crabdance.com";
    public final static String user = "QL0";
    public final static String host = "192.168.99.1";
    private static boolean internalStorage = true;
    private static String storageDir;
    public static String getInfoFilePath(){
        return getStorageDir() + sep  + INFO_FILE_NAME;
    }

    public static String getActualFile(Context context) {
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier("id_rsa",
                        "raw", context.getPackageName()));

        String fStr = Config.getStorageDir() + sep + fileName;


        if(!new File(fStr).exists()){
            try {
                Util.writeToFile(ins,fStr);
            } catch (IOException e) {
                e.printStackTrace();//USe android log here
            }
        }
        return getStorageDir() + sep  + fileName;
    }

    public static String getSendFile() {
        return getStorageDir() + sep  + SSH_FILE_NAME;
    }
    public static String getStorageDir(){
       return storageDir;
    }
    public static void initStorage(Context context){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            internalStorage = false;
            storageDir = Environment.getExternalStorageDirectory().getPath()+ sep + APP_DIR;
        }else{
            internalStorage = true;
            //external not available so use internal
            storageDir = context.getFilesDir().getPath() + sep + APP_DIR;
        }
        new File(storageDir).mkdir();

    }
    public static boolean isInternalStorage() {
        return internalStorage;
    }
}
