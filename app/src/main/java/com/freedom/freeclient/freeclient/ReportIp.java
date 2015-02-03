package com.freedom.freeclient.freeclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kemihambolu on 1/24/15.
 */
public class ReportIp {
    static String inputLine;

    public static String returnIp(URL url){
        System.out.println("Herererere3 ");

        try{
            url = new URL("http://curlmyip.com/");
            // get URL content
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(url.openStream()));



            inputLine = br.readLine();

            br.close();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputLine;
    }
}
