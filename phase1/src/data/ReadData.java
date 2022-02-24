package data;

import back_end.TagManager;
import back_end.Tags;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/** A class to read data from .ser files in src/data */
public class ReadData {

    /** Method reads the serialized file name change logs ArrayList in data/logs.ser
     *
     * @return a ArrayList of String[] where String[0] is previous name, String[1] is new name and String[2] is a
     *         timestamp for each String[] in the ArrayList
     */
    public ArrayList<String[]> readLogs() {
        try {
            ArrayList<String[]> logList;
            File initialFile = new File("data" + File.separator + "logs.ser");
            InputStream targetStream = new FileInputStream(initialFile);
            if (targetStream.available() != 0) {
                ObjectInputStream ois = new ObjectInputStream(targetStream);
                logList = (ArrayList<String[]>) ois.readObject();
                ois.close();
                return logList;
            } else {
                logList = new ArrayList<String[]>();
                return logList;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
        }

    /** Method reads the serialized tags ArrayList in data/tags.ser
     *
     * @return a ArrayList of Strings of all tags added by the user
     */
    public ArrayList<String> readTags() {
        ArrayList<String> tagsIn = new ArrayList<>();
        try {
            File tagsFile = new File("data" + File.separator + "tags.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
            }
            else {
                FileInputStream fileIn = new FileInputStream("data" + File.separator + "tags.ser");
                if (fileIn.available() != 0) {
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    tagsIn = (ArrayList<String>) in.readObject();
                    TagManager.setTags(tagsIn.toArray(new String[tagsIn.size()]));
                    fileIn.close();
                }
            }

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return tagsIn;
    }

    /** Method reads the serialized HashMap of revisions of image names in data/revisions.ser
     *
     * @return A HashMap of all image name changes; String is the absolute path of the image, ArrayList<<Tags> is a list
     * of tags for the image
     */
    public HashMap<String, ArrayList<Tags>> readRevisions() {
        HashMap<String, ArrayList<Tags>> revisionsIn = new HashMap<>();
        File revisionsFile = new File("data" + File.separator + "revisions.ser");
        try {
            if (!revisionsFile.exists()) {
                revisionsFile.createNewFile();
            }
            FileInputStream fileIn = new FileInputStream("data" + File.separator + "revisions.ser");
            if (revisionsFile.length() != 0) {
                ObjectInputStream in = new ObjectInputStream(fileIn);
                revisionsIn = (HashMap<String, ArrayList<Tags>>) in.readObject();
                in.close();
                fileIn.close();
            }
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (IOException i){
            i.printStackTrace();
        }
        return revisionsIn;
    }
}



