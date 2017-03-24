package io.github.bfvstats.dbfiller;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

// normal maps on everything on highest settings, even on leaves and trees

@Log
public class FtpDownloader {
  public static void main(String[] args) throws IOException {
    downloadFiles("ftpconfig.properties");
  }

  public static void downloadFiles(String configFilename) throws IOException {
    Properties props = new Properties();
    props.load(new FileInputStream(configFilename));

    ConnectDetails connectDetails = new ConnectDetails()
        .setServerAddress(props.getProperty("serverAddress").trim())
        .setUserId(props.getProperty("userId").trim())
        .setPassword(props.getProperty("password").trim())
        .setRemoteDirectory(props.getProperty("remoteDirectory").trim())
        .setLocalDirectory(props.getProperty("localDirectory").trim());

    try {
      downloadFiles(connectDetails);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Accessors(chain = true)
  @Data
  public static class ConnectDetails {
    String serverAddress;
    String userId;
    String password;
    String remoteDirectory;
    String localDirectory;
  }

  public static boolean downloadFiles(ConnectDetails connectDetails) throws IOException {
    //new ftp client
    FTPClient ftpClient = new FTPClient();
    ftpClient.setBufferSize(1024000);

    //try to connect
    ftpClient.connect(connectDetails.getServerAddress());
    //login to server
    if (!ftpClient.login(connectDetails.getUserId(), connectDetails.getPassword())) {
      ftpClient.logout();
      return false;
    }
    int reply = ftpClient.getReplyCode();
    //FTPReply stores a set of constants for FTP reply codes.
    if (!FTPReply.isPositiveCompletion(reply)) {
      ftpClient.disconnect();
      return false;
    }

    ftpClient.enterLocalPassiveMode();

    ftpClient.changeWorkingDirectory(connectDetails.getRemoteDirectory());
    System.out.println("Current directory is " + ftpClient.printWorkingDirectory());

    FTPFileFilter logFilesFilter = file -> file != null &&
        file.isFile() &&
        file.getName().startsWith("ev_");

    FTPFile[] array = ftpClient.listFiles(null, logFilesFilter);
    List<FTPFile> filesOrderedByNameAsc = Arrays.stream(array)
        .sorted(Comparator.comparing(FTPFile::getName))
        .collect(Collectors.toList());

    for (FTPFile ftpFile : filesOrderedByNameAsc) {
      String filename = ftpFile.getName();
      Path localFilePath = Paths.get(connectDetails.getLocalDirectory(), filename);
      if (!Files.exists(localFilePath) || Files.size(localFilePath) != ftpFile.getSize()) {
        log.info(filename);

        OutputStream output = Files.newOutputStream(localFilePath, TRUNCATE_EXISTING, CREATE);
        ftpClient.retrieveFile(ftpFile.getName(), output);
        output.close();
      }
    }

    ftpClient.logout();
    ftpClient.disconnect();

    return true;
  }
}
