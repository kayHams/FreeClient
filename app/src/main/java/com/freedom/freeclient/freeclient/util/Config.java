package com.freedom.freeclient.freeclient.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class Config {
    public static Context myContext;
    public static String sep = System.getProperty("file.separator");
    public static final String STORAGE_DIR = Environment.getExternalStorageDirectory().getPath();
    //public static final String STORAGE_DIR = myContext.getFilesDir().getParentFile().getPath();
    public static final String REAL_DOMAIN_FILE = "domain.txt";
    public static final String SSH_FILE_NAME = "username.txt";
    public static final String INFO_FILE_NAME = "option.props";
    public static final String SPEEED_FILE_NAME = "speed.txt";
    public static final String APP_DIR="freeclient";
    public static final String fileName = "ssh/id_rsa";
    public static final String INFO_FILE_PATH = STORAGE_DIR + sep + APP_DIR + sep  + INFO_FILE_NAME;
    public static final String domainName = "ifserver.crabdance.com";
    public final static String actualFile = STORAGE_DIR + sep  + fileName;
    public final static String sendFile = STORAGE_DIR + sep  + INFO_FILE_NAME;
    public final static String user = "kemi";
    public final static String host = "130.127.24.141";

}
