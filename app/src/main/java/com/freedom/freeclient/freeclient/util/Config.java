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

    public static String getInfoFilePath(Context context){
        return getStorageDir(context) + sep + APP_DIR + sep  + INFO_FILE_NAME;
    }

    public static String getActualFile(Context context) {
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier("id_rsa",
                        "raw", context.getPackageName()));

        String fStr = Config.getStorageDir(context) + sep + fileName;


        if(!new File(fStr).exists()){
            try {
                Util.writeToFile(ins,Config.getStorageDir(context) + sep + fileName);
            } catch (IOException e) {
                e.printStackTrace();//USe android log here
            }
        }
        return getStorageDir(context) + sep  + fileName;
    }

    public static String getSendFile(Context c) {
        return getStorageDir(c) + sep  + SSH_FILE_NAME;
    }
    //TODO: change this to set storage directory. Call it only once at beginning of main. And provide a separate getStorageDir()
    public static String getStorageDir(Context context){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            internalStorage = false;
            return Environment.getExternalStorageDirectory().getPath();
        }

        internalStorage = true;
        //external not available so use internal
        return context.getFilesDir().getParentFile().getPath();
    }

    public static boolean isInternalStorage() {
        return internalStorage;
    }
}
