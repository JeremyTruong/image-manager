package controller;

import model.data.ReadData;
import model.data.WriteData;
import java.util.ArrayList;
import java.util.Date;

/**
 * The LogManager class contains allLogs, a list of naming changes to Pictures, and adds to the list
 * when new changes occur.
 */
public class LogManager {
  private static ArrayList<String[]> allLogs = new ArrayList<>();

  /**
   * Returns allLogs, the list of all logs of Picture naming changes.
   *
   * @return list of all logs
   */
  public static ArrayList<String[]> getAllLogs() {
    return allLogs;
  }

  /**
   * Updates allLogs with a new naming change. The oldName argument is the name of the Picture
   * before the change. The newName argument is the name of the Picture after the change. The log
   * update is stored to a file.
   *
   * @param oldName string representing current name of a Picture
   * @param newName string representing the new name of a Picture
   */
  public static void addLog(String oldName, String newName) {
    String[] logArray = new String[3];
    Date date = new Date();
    logArray[0] = oldName;
    logArray[1] = newName;
    logArray[2] = date.toString();
    allLogs.add(logArray);
    serializeArrayList();
  }

  /** Writes allLogs to a file for storage. */
  private static void serializeArrayList() {
    // creating output stream variables
    WriteData writer = new WriteData();
    writer.writeLogs(allLogs);
  }

  /** Reads a file containing stored logs and stores the logs in allLogs. */
  public void readSerializedArrayList() {
    ReadData reader = new ReadData();
    allLogs = reader.readLogs();
  }
}
