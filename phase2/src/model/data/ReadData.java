package model.data;

import controller.TagManager;
import model.Tags;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The ReadData class has methods used for reading files with logs, tags, and Picture name revisions.
 */
public class ReadData {

    /**
     * Returns the serialized ArrayList of logs of name revisions read from model.data/logs.ser. This is a log listing all
     * naming revisions.
     *
     * @return a ArrayList of String[] where String[0] is previous name, String[1] is new name and String[2] is a
     *         timestamp for each String[] in the ArrayList
     */
    public ArrayList<String[]> readLogs() {
        File initialFile = new File("model/data" + File.separator + "logs.ser");
        if (initialFile.exists()) {
            try {
                ArrayList<String[]> logList;
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
        }
        return new ArrayList<>();

    }

    /**
     * Returns the serialized ArrayList of tags read from model.data/tags.ser. This is a list of all tags used in the
     * application.
     *
     * @return a ArrayList of Strings of all tags added by the user
     */
    public HashSet<String> readTags() {
        HashSet<String> tagsIn = new HashSet<>();
        try {
            File tagsFile = new File("model/data" + File.separator + "tags.ser");
            if (!tagsFile.exists()) {
                tagsFile.createNewFile();
                return new HashSet<>();
            }
            else {
                FileInputStream fileIn = new FileInputStream("model/data" + File.separator + "tags.ser");
                if (fileIn.available() != 0) {
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    tagsIn = (HashSet<String>) in.readObject();
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

    /**
     * Returns the serialized HashMap of revisions of picture names read from data/revisions.ser if the file exists.
     * This is a HashMap mapping all picture file names to their name revision history. If the file does not exist, it
     * is created.
     *
     * @return A HashMap of all image name changes where String is the absolute path of the image, ArrayList<<Tags> is
     * a list of tag revisions for the image
     */
    public HashMap<String, ArrayList<Tags>> readRevisions() {
        HashMap<String, ArrayList<Tags>> revisionsIn = new HashMap<>();
        File revisionsFile = new File("model/data" + File.separator + "revisions.ser");
        try {
            if (!revisionsFile.exists()) {
                revisionsFile.createNewFile();
            }
            FileInputStream fileIn = new FileInputStream("model/data" + File.separator + "revisions.ser");
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



