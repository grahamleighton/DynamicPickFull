package com.ottouk.pdcu.version.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @author dis114
 *
 */
public class PDCUftp {
                /** ftp server name. **/
      private static String server;
                /** ftp user name. **/
      private static String username;
               /** ftp user password. **/
      private static String password;
      
      /**
       * Constructor - sets instance variables.
       * 
       * @param mySvr     - The name of the ftp server to connect to.
       * @param myUsr     - The user name of the ftp account.
       * @param myPass    - The password for the ftp account.
       */
      
      public PDCUftp(final String mySvr, final String myUsr, final String myPass) {
          server = mySvr;
          username = myUsr;
          password = myPass;
      }
      
      /**
       * getDataFile - Gets a file from the ftp server.
       * 
       * @param folder - the source directory on the ftp server
       * @param fileName - the filename to be transferred from the ftp server
       * @param destinationFile - the destination filename.
       * 
       * @return true - the get succeeded, false otherwise.
       */
      
      public final boolean getDataFile(final String folder,
                                        final String fileName,
                                        final String destinationFile) {
        boolean successStatus = true;
        FTPClient ftp = new FTPClient();

        try {
        
          // Connect and logon to FTP Server

          ftp.connect(server);
          ftp.login(username, password);
          // System.out.println("Connected to " + server + ".");

          // After connection attempt, you should check the reply code to verify
          // success.
          int reply = ftp.getReplyCode();
          if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.out.println("FTP server refused connection.");
            return false;
          }
          System.out.println("FTPing ... " + fileName + " to " + destinationFile);
          ftp.changeWorkingDirectory(folder);

          File file = new File(destinationFile + ".new");  // temp destination file

          FileOutputStream fos = new FileOutputStream(file); 
          successStatus = ftp.retrieveFile(fileName , fos);  // ftp fileName to temp destination file 
          fos.close();
              
  
          if (successStatus) {
                  //System.out.println("FTP OK");
                  
                  // Delete existing file then rename temp destination file to real filename
                  // done this to avoid partial overwrite of destination file if ftp failed part way.
                  File target = new File(destinationFile); 
                  if (!target.exists()) {
                    System.out.println("File " + destinationFile + " not present to begin with!");
                  } else {
                      // now, delete it immediately:
                      if (target.delete()) {
                          System.out.println("** Deleted " + destinationFile + " **");
                      } else {
                          System.out.println("Failed to delete " + destinationFile);
                      }
                  }
                  
                  // Attempt to rename    
                  if (file.renameTo(new File(destinationFile))) {
                          System.out.println("RENAME OK");
                  }
             
              } else {
                  System.out.println("FTP FAILED");
              }


          // Logout from the FTP Server and disconnect
          ftp.logout();
          ftp.disconnect();

          return successStatus;

        
      } catch (IOException e) {
       
        e.printStackTrace();
        
        return false;
        
      } finally {
        if (ftp.isConnected()) {
          try {
            ftp.disconnect();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      }

      } 
      
      
      
      
      
      /**
       * putDataFile - Stores a file on the ftp server.
       * 
       * @param folder - the destination directory on the ftp server
       * @param fileName - the filename to be transferred to the ftp server
       * @param destinationFile - the destination filename.
       * 
       * @return true - the get succeeded, false otherwise.
       */
      
      public final boolean putDataFile(final String folder,
                                       final String fileName,
                                       final String destinationFile) {
        boolean successStatus = true;
        FTPClient ftp = new FTPClient();

        try {
        
          // Connect and logon to FTP Server

          ftp.connect(server);
          ftp.login(username, password);
          // System.out.println("Connected to " + server + ".");

          // After connection attempt, you should check the reply code to verify
          // success.
          int reply = ftp.getReplyCode();
          if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.out.println("FTP server refused connection.");
            return false;
          }
          System.out.println("FTPing ... " + fileName + " to " + destinationFile);
          ftp.changeWorkingDirectory(folder);
          try {
              File file = new File(fileName);  // temp destination file

              FileInputStream fis = new FileInputStream(file);
          
              successStatus = ftp.storeFile(destinationFile , fis);  // ftp fileName to temp destination file 
              fis.close();
              
              // Logout from the FTP Server and disconnect
              ftp.logout();
              ftp.disconnect();
          } catch (Exception e) {
              //e.printStackTrace();
              return false;
          }

          return successStatus;

        
      } catch (IOException e) {
       
        e.printStackTrace();
        
        return false;
        
      } finally {
        if (ftp.isConnected()) {
          try {
            ftp.disconnect();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      }

      } 
}
