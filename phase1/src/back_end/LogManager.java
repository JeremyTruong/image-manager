package back_end;

import data.ReadData;
import data.WriteData;
import java.util.ArrayList;
import java.util.Date;

/** A class to handle logging of file name changes*/
public class LogManager {

    public static ArrayList<String[]> allLogs = new ArrayList<>();

    /** Method logs a change to allLogs ArrayList of string arrays
     *
     * @param oldName string representing current name of an image
     * @param newName string representing the new name of an image
     */
    static void addLog(String oldName, String newName) {
        String[] logArray = new String[3];
        Date date = new Date();
        logArray[0] = oldName;
        logArray[1] = newName;
        logArray[2] = date.toString();
        allLogs.add(logArray);
        serializeArrayList();
    }

    /** Method takes the ArrayList allLogs serializes it and writes it to data/logs.ser */
    private static void serializeArrayList() {
        // creating output stream variables
        WriteData writer = new WriteData();
        writer.writeLogs(allLogs);
    }

    /** Method reads the serialized list of logs in data/logs.ser */
    public void readSerializedArrayList() {
        ReadData reader = new ReadData();
        allLogs = reader.readLogs();
    }

}
