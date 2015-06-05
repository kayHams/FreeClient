package com.freedom.freeclient.freeclient;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.Util;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


import java.io.BufferedInputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class ConnectActivity extends ActionBarActivity {
    File proxyFile = new File(Config.getStorageDir() + Config.sep + "proxy.txt");
    File clientFile = new File(Config.getSendFile());
    private ProgressDialog progressDialog;
    Handler handler = new Handler(Looper.getMainLooper());
    final Context context = this;
    private static final int SETTINGS_REQUEST =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        final Button button = (Button) findViewById(R.id.BtnReStart);
        final Properties props = Util.getProperties(Config.getInfoFilePath());


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new StartConnection(props).execute(1);

            }
        });


        // check if the user has entered their setting information, if not, go to settingsActivity, if yes, connect to iodine
        if(props.get("country") == null || props.get("country").equals("") ){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(settingsIntent, SETTINGS_REQUEST);
        }else{
           new CreateConnection(props).execute(1);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SETTINGS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {//settings was saved successfully
                final Properties props = Util.getProperties(Config.getInfoFilePath());
            }
        }
    }

    public boolean connect(Properties props){

        String country_code = (String) props.get("country");

        String locale = this.getResources().getConfiguration().locale.getCountry();
        int c = ClientLocation.MapCodeToCountry(locale);
        String countryNum = Integer.toString(c);

        String written_ip = "0.0.0.0";
        Util.writeToClientFile(country_code,countryNum,written_ip, Config.my_speed);


        return false;

    }
    class CreateConnection extends AsyncTask<Integer, Void, Void> {
        private Properties props;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ConnectActivity.this, "Please wait..", "Connecting...", true);
            super.onPreExecute();
        }

        CreateConnection(Properties props){
            this.props = props;
        }
        protected Void doInBackground(Integer... params) {
            connect(props);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            new StartConnection(props).execute(1);
            //super.onPostExecute(aVoid);
        }
    }
    class TestUrlConnection extends AsyncTask<String, Void, Boolean> {
        private Properties props;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... url) {
            return checkValidConnection(url[0]);
        }
        boolean checkValidConnection(String url){
            int code = -1;
            URL u = null;
            HttpsURLConnection huc = null;
            try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }});
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new X509TrustManager(){
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }}}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                        context.getSocketFactory());
            } catch (Exception e) { // should never happen
                e.printStackTrace();
            }
            try {
                u = new URL(url);
                huc = (HttpsURLConnection)u.openConnection();
                //huc.setSSLSocketFactory(context.getSocketFactory());
                huc.setRequestMethod("HEAD");
                huc.connect() ;
                code = huc.getResponseCode();
                System.out.println("The code is " + code);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return code == HttpURLConnection.HTTP_OK;
        }
    }

    class StartConnection extends AsyncTask<Integer, Void, Void> {
        private Properties props;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ConnectActivity.this, "Please wait..", "Connecting...", true);
            super.onPreExecute();
        }
        StartConnection(Properties props){
            this.props = props;
        }
        private boolean conn = false;

        protected Void doInBackground(Integer... params) {
            conn = sftp_A2RS();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(conn){
                CheckUrl();
            }else{
                stopNoAndiodine();
            }
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
    public boolean sftp_A2RS()
    {
        Session session =null;
        FileInputStream fis=null;
        //Channel channel = null;
        try {
            JSch jsch = new JSch();

            int port = 22;
            //String privateKey = ".ssh/id_rsa";

            jsch.addIdentity(Config.getActualFile(ConnectActivity.this));
            System.out.println("identity added ");

            session = jsch.getSession(Config.user, Config.host, port);
            System.out.println("session created.");

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            System.out.println("session connected.....");

            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("shell channel connected....");

            ChannelSftp c = (ChannelSftp) channel;
            c.cd("/home/" + Config.user + "/user_data/");
            File f = new File(Config.getSendFile());
            fis = new FileInputStream(f);

            c.put(fis,f.getName());
            fis.close();
            System.out.println("done");
            SystemClock.sleep(10000);
            c.get("/home/" + Config.user + "/proxy_data/proxy.txt", Config.getProxyFile());
            System.out.println("done");
            c.exit();
            channel.disconnect();

        } catch (Exception e) {
            System.err.println(e);
        }

        if(session != null) return session.isConnected();

        return false;
    }

   public void CheckUrl(){
        String url1 =  Util.readFromProxyFile(proxyFile);
        String url2 =  Util.readFromProxyFile1(proxyFile);
       //startBrowser(url1);
       if(exists(url1)){
           startBrowser(url1);
       }else if(exists(url2)){
           startBrowser(url2);
       }
       else{
           Toast.makeText(ConnectActivity.this, "The proxy is currently down. Please contact us", Toast.LENGTH_LONG).show();
           ConnectActivity.this.finish();}
    }
    public boolean exists(String url)
    // To check if server is reachable
    {
        boolean exist = false;
        try{
            exist = new TestUrlConnection().execute(url).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return exist;
    }
    public void startBrowser(String url){

        Toast.makeText(ConnectActivity.this, "Please Stop The Andiodne Service Now!", Toast.LENGTH_LONG).show();
        Uri uriUrl = Uri.parse(url+"/en/20/https/duckduckgo.com/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
        ConnectActivity.this.finish();
    }



    public void stopNoAndiodine() {
        Toast.makeText(ConnectActivity.this, "Andiodine is not running, Please ensure andiodine has started without errors before starting this application", Toast.LENGTH_LONG).show();
        ConnectActivity.this.finish();
    }


}
