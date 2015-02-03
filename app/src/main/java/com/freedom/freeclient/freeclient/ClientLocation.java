package com.freedom.freeclient.freeclient;

import android.os.Environment;
import android.util.Log;

import java.io.IOException;

import com.freedom.freeclient.freeclient.util.Config;
import com.maxmind.geoip.*;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class ClientLocation {
    private static String code;
    private static int number;
    private static String sep = System.getProperty("file.separator");
    //private static String dir = "~/AndroidStudioProjects/FreeClient2/GeoIP";
    //private static String path = Environment.getExternalStorageDirectory().getPath();
    private static String dbfile = Config.STORAGE_DIR + sep + "GeoIP.dat";
    public static String CountryCode(String ip){
        Log.e("DEBUG_MSG","GOT HEREEEEEEEEEE2");
        try {

            //code = LookupService.getCountry(ip).getCode();

            LookupService cl = new LookupService(dbfile,LookupService.GEOIP_MEMORY_CACHE);

            code = cl.getCountry(ip).getCode();

            Log.e("DEBUG_MSG",code);

            cl.close();
        }
        catch (IOException e) {
            System.err.println("An IOException was caught :"+e.getMessage());
           // System.out.println("IO Exception");
            //e.getMessage();
        }
        return code;

    };

    public static int MapCodeToCountry(String code){

        Log.e("DEBUG_MSG", "GOT HEREEEEEEEEEE3");

        String c = code, bj = "BJ", bf = "BF", cm ="CM", td = "TD", ci = "CI", dj ="DJ", fr = "FR", gq ="GQ", gm = "GM", gn = "GN", lr = "LR", nl ="NL", ng = "NG",no ="NO",sn = "SN", tg = "TG", ug = "UG", ga = "GA", cf = "CF",us ="US";
        if(c.equals(bj)){
            number = 1;
        }else if (c.equals(bf)){
            number = 2;
        }else if (c.equals(cm)){
            number = 3;
        }else if (c.equals(td)){
            number = 4;
        }else if (c.equals(ci)){
            number = 5;
        }else if (c.equals(dj)){
            number = 6;
        }else if (c.equals(fr)){
            number = 7;
        }else if (c.equals(gq)){
            number = 8;
        }else if (c.equals(gm)){
            number = 9;
        }else if (c.equals(gn)){
            number = 10;
        }else if (c.equals(lr)){
            number = 10;
        }else if (c.equals(nl)){
            number = 12;
        }else if (c.equals(ng)){
            number = 13;
        }else if (c.equals(no)){
            number = 14;
        }else if (c.equals(sn)){
            number = 15;
        }else if (c.equals(tg)){
            number = 16;
        }else if (c.equals(ug)){
            number = 17;
        }else if (c.equals(ga)){
            number = 18;
        }else if (c.equals(cf)){
            number = 19;
        }else if (c.equals(us)){
            number = 20;
        }else {number = 21;}

        //System.out.println("number = (%i)" %number);
        return number;
    };

}