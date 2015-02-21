package com.freedom.freeclient.freeclient;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.freedom.freeclient.freeclient.util.Config;
import com.freedom.freeclient.freeclient.util.Util;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class PasswordlessSSHManager {
	
private static final Logger LOGGER = Logger.getLogger(PasswordlessSSHManager.class.getName());
private JSch jschSSHChannel;
private String strUserName;
private String strConnectionIP;
private int intConnectionPort;
private String strPrivateKeyPath;
private String strPassphrase;
private Session sesConnection;
private int intTimeOut;
private String SourceFilePath;
private String DestinationFilePath;

private void doCommonConstructorActions(String userName, 
     String privateKeyPath, String passphrase, String connectionIP, String knownHostsFileName)
{
   jschSSHChannel = new JSch();

   try
   {
      jschSSHChannel.setKnownHosts(knownHostsFileName);
   }
   catch(JSchException jschX)
   {
      logError(jschX.getMessage());
   }

   strUserName = userName;
   strPrivateKeyPath = privateKeyPath;
   strPassphrase = passphrase;
   strConnectionIP = connectionIP;
}

public PasswordlessSSHManager(String userName, String privateKeyPath, 
		String passphrase, String connectionIP, String knownHostsFileName)
{
   doCommonConstructorActions(userName, privateKeyPath, passphrase, 
              connectionIP, knownHostsFileName);
   intConnectionPort = 22;
   intTimeOut = 60000;
}

public PasswordlessSSHManager(String userName, String privateKeyPath, String passphrase, String connectionIP, 
   String knownHostsFileName, int connectionPort)
{
   doCommonConstructorActions(userName, privateKeyPath, passphrase, connectionIP, 
      knownHostsFileName);
   intConnectionPort = connectionPort;
   intTimeOut = 60000;
}

public PasswordlessSSHManager(String userName, String privateKeyPath, String passphrase, String connectionIP, 
    String knownHostsFileName, int connectionPort, int timeOutMilliseconds)
{
   doCommonConstructorActions(userName, privateKeyPath, passphrase, connectionIP, 
       knownHostsFileName);
   intConnectionPort = connectionPort;
   intTimeOut = timeOutMilliseconds;
}

public String connect()
{
   String errorMessage = null;

   try
   {
	   if (strPassphrase == null){
		   jschSSHChannel.addIdentity(strPrivateKeyPath);
	   }
	   else{
		   jschSSHChannel.addIdentity(strPrivateKeyPath, strPassphrase);
	   }
	   sesConnection = jschSSHChannel.getSession(strUserName, 
          strConnectionIP, intConnectionPort);
      // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
      //sesConnection.setConfig("StrictHostKeyChecking", "no");
	   
      sesConnection.connect(intTimeOut);
   }
   catch(JSchException jschX)
   {
      errorMessage = jschX.getMessage();
   }

   return errorMessage;
}

private String logError(String errorMessage)
{
   if(errorMessage != null)
   {
      LOGGER.log(Level.SEVERE, "{0}:{1} - {2}", 
          new Object[]{strConnectionIP, intConnectionPort, errorMessage});
   }

   return errorMessage;
}

private String logWarning(String warnMessage)
{
   if(warnMessage != null)
   {
      LOGGER.log(Level.WARNING, "{0}:{1} - {2}", 
         new Object[]{strConnectionIP, intConnectionPort, warnMessage});
   }

   return warnMessage;
}

public String sendCommand(String command)
{
   StringBuilder outputBuffer = new StringBuilder();

   try
   {
      Channel channel = sesConnection.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);
      channel.connect();
      InputStream commandOutput = channel.getInputStream();
      int readByte = commandOutput.read();

      while(readByte != 0xffffffff)
      {
         outputBuffer.append((char)readByte);
         readByte = commandOutput.read();
      }

      channel.disconnect();
   }
   catch(IOException ioX)
   {
      logWarning(ioX.getMessage());
      return null;
   }
   catch(JSchException jschX)
   {
      logWarning(jschX.getMessage());
      return null;
   }

   return outputBuffer.toString();
}

public static void sftp_A2RS(Context context)
{
    try {
        JSch jsch = new JSch();

        int port = 22;
        //String privateKey = ".ssh/id_rsa";

        jsch.addIdentity(Config.getActualFile(context));
        System.out.println("identity added ");

        Session session = jsch.getSession(Config.user, Config.host, port);
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

        //String fileName = "test.txt";
        c.put(Config.getSendFile(context), "/home/" + Config.user + "/user_data");
        System.out.println("done");
        SystemClock.sleep(20000);
        c.get("/home/" + Config.user + "/proxy_data/proxy.txt", Config.getSendFile(context));
        c.exit();
        System.out.println("done");
        channel.disconnect();

    } catch (Exception e) {
        System.err.println(e);
    }
}

public void sftp_RS2A(String remoteServerPath, String androidPath)
{
	try {
		Channel channel = sesConnection.openChannel("sftp");
		channel.connect();
		
		ChannelSftp c = (ChannelSftp) channel;
		
		try {
			SourceFilePath = androidPath;
			DestinationFilePath = remoteServerPath;
			
			// set remote file
			InputStream in = c.get(DestinationFilePath);
			
			// set local file
			//String lf = "OBJECT_FILE";
			FileOutputStream targetFile = new FileOutputStream(SourceFilePath);
			
			// read containts of remote file to local
			int cn;
			while ((cn= in.read()) != -1){
				targetFile.write(cn);
			}
			
			in.close();
			targetFile.close();			
			
			c.exit();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		channel.disconnect(); 
		
	} catch (JSchException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}


public void close()
{
   sesConnection.disconnect();
}

}