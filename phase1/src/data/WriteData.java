package data;
import back_end.Tags;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/** A class to read data from .ser files in src/data */
public class WriteData {

    /** Method writes a serialized ArrayList<String> to data/tags.ser
     *
     * @param tags a ArrayList of strings representing available tags for the user to choose from
     */
    public void writeTags(ArrayList<String> tags) {
        try {
            File tagsFile = new File("data/tags.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream("data/tags.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tags);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method writes a serialized ArrayList<String[]> to data/logs.ser
     *
     * @param logsArray a ArrayList of String[] where String[0] is previous name, String[1] is new name and String[2] is a
     *         timestamp for each String[] in the ArrayList
     */
    public void writeLogs(ArrayList<String[]> logsArray) {
        try {
            FileOutputStream fos;
            ObjectOutputStream oos;
            File tagsFile = new File("data/logs.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            // for writing or saving binary data
            fos = new FileOutputStream("data/logs.ser");

            // converting java-object to binary-format
            oos = new ObjectOutputStream(fos);

            // writing or saving ArrayList values to stream
            oos.writeObject(logsArray);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method writes a serialized HashMap<String, ArrayList<Tags>> to revisions/logs.ser
     *
     * @param revisionsMap a HashMap mapping the absolute path of image files to the list of their naming history
     */
    public void writeRevisions(HashMap<String, ArrayList<Tags>> revisionsMap) {
        try {
            FileOutputStream fos;
            ObjectOutputStream oos;
            File tagsFile = new File("data/revisions.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            // for writing or saving binary data
            fos = new FileOutputStream("data/revisions.ser");

            // converting java-object to binary-format
            oos = new ObjectOutputStream(fos);

            // writing or saving ArrayList values to stream
            oos.writeObject(revisionsMap);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
