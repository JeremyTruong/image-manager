package model.data;
import model.Tags;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The WriteData class has methods for writing files of logs, tags, and Picture name revisions.
 */
public class WriteData {

    /**
     * Writes the argument tags, a serialized ArrayList of tags, to model.data/tags.ser. This is the list of all tags used in the application.
     * exist. This is the list of all tags used in the application.
     *
     * @param tags a ArrayList of strings representing available tags for the user to choose from
     */
    public void writeTags(HashSet<String> tags) {
        try {
            File tagsFile = new File("model/data/tags.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream("model/data/tags.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tags);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the argument logsArray, a serialized ArrayList of logs, to model.data/logs.ser. This is a log listing all naming revisions.
     * exist. This is a log listing all naming revisions.
     *
     * @param logsArray a ArrayList of String[] where String[0] is previous name, String[1] is new name and String[2] is
     *                  a timestamp for each String[] in the ArrayList
     */
    public void writeLogs(ArrayList<String[]> logsArray) {
        try {
            FileOutputStream fos;
            ObjectOutputStream oos;
            File tagsFile = new File("model/data/logs.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            // for writing or saving binary model.data
            fos = new FileOutputStream("model/data/logs.ser");

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

    /**
     * Writes the argument revisionsMap, a serialized HashMap<String, ArrayList<Tags>>, to revisions/logs.ser, creating
     * the file if it does not exist. This is a HashMap mapping all picture file names to their name revision history.
     *
     * @param revisionsMap a HashMap mapping the absolute path of image files to the list of their naming history
     */
    public void writeRevisions(HashMap<String, ArrayList<Tags>> revisionsMap) {
        try {
            FileOutputStream fos;
            ObjectOutputStream oos;
            File tagsFile = new File("model/data/revisions.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            // for writing or saving binary model.data
            fos = new FileOutputStream("model/data/revisions.ser");

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
