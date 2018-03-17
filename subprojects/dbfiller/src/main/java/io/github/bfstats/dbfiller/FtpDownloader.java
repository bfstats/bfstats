package io.github.bfstats.dbfiller;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

// normal maps on everything on highest settings, even on leaves and trees

@Log
public class FtpDownloader {
  public static void main(String[] args) throws IOException {
    List<ConnectDetails> connectDetailsList = loadConfigProperties();
    for (ConnectDetails connectDetails : connectDetailsList) {
      downloadFiles(connectDetails);
    }
  }

  private static ConnectDetails mapToConnectDetails(Map<String, String> props) {
    GameServerDetails gameServerDetails = new GameServerDetails()
        .setGameServerAddress(props.get("gameServerAddress").trim())
        .setGameServerPort(Integer.parseInt(props.get("gameServerPort").trim()))
        .setGameServerTimezone(props.get("gameServerTimezone").trim());

    return new ConnectDetails()
        .setServerAddress(props.get("serverAddress").trim())
        .setUserId(props.get("userId").trim())
        .setPassword(props.get("password").trim())
        .setRemoteDirectory(props.get("remoteDirectory").trim())
        .setLocalDirectory(props.get("localDirectory").trim())
        .setFtps(props.getOrDefault("isFtps", "false").equals("true"))
        .setDownload(props.getOrDefault("download", "false").equals("true"))
        .setGameServerDetails(gameServerDetails);
  }

  public static List<ConnectDetails> loadConfigProperties() throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream configFileInputStream = loader.getResourceAsStream("ftpconfig.properties");
    Properties props = new Properties();
    props.load(configFileInputStream);

    Map<String, Map<String, String>> map = new HashMap<>();

    props.forEach((key, propertyValue) -> {
      if (key instanceof String) {
        String keyStr = (String) key;
        if (keyStr.startsWith("server.")) {
          String[] keyParts = keyStr.split("\\.", 10);
          String serverKey = keyParts[1];
          String propertyName = keyParts[2];
          map.computeIfAbsent(serverKey, k -> new HashMap<>())
              .put(propertyName, (String) propertyValue);
        }
      }
    });

    return map.values()
        .stream()
        .map(FtpDownloader::mapToConnectDetails)
        .collect(toList());
  }

  @Accessors(chain = true)
  @Data
  public static class ConnectDetails {
    String serverAddress;
    String userId;
    String password;
    String remoteDirectory;
    String localDirectory;

    boolean ftps;
    boolean download;

    GameServerDetails gameServerDetails;
  }

  @Accessors(chain = true)
  @Data
  public static class GameServerDetails {
    String gameServerAddress;
    int gameServerPort;
    String gameServerTimezone;
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
    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    ftpClient.changeWorkingDirectory(connectDetails.getRemoteDirectory());
    System.out.println("Current directory is " + ftpClient.printWorkingDirectory());

    FTPFileFilter logFilesFilter = file -> file != null &&
        file.isFile() &&
        file.getName().startsWith("ev_");

    FTPFile[] array = ftpClient.listFiles(null, logFilesFilter);
    List<FTPFile> filesOrderedByNameAsc = Arrays.stream(array)
        .sorted(Comparator.comparing(FTPFile::getName))
        .collect(toList());

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
