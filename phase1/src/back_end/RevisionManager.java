package back_end;

import data.ReadData;
import data.WriteData;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/** A class to handle all tag revisions */
public class RevisionManager implements Serializable{
    private static HashMap<String, ArrayList<Tags>> revisions = new HashMap<>();

    /**
     * Returns the HashMap containing all tag revisions stored.
     *
     * @return HashMap with String keys and ArrayList<Tags> values containing all tag revisions
     */
    public static HashMap<String, ArrayList<Tags>> getRevisions() {
        readRevisionsFromFile();
        return revisions;
    }

    /**
     * Sets revisions with revisionsIn.
     *
     * @param revisionsIn HashMap that revisions is set to
     */
    private static void setRevisions(HashMap<String, ArrayList<Tags>> revisionsIn) {
        revisions = revisionsIn;
    }

    /**
     * Updates revisions of a picture with new tags.
     *
     * @param oldPath String of file's old location
     * @param newPath String of file's new location
     * @param tags Tags of the newest tag revision
     */
    static void updateRevisions(String oldPath, String newPath, Tags tags) {
        if (revisions.containsKey(oldPath)) {
            ArrayList<Tags> keepTags = revisions.get(oldPath);
            if (tags != null) {
                keepTags.add(tags);
            }
            revisions.remove(oldPath);
            revisions.put(newPath, keepTags);
        } else {
            ArrayList<Tags> newValue = new ArrayList<>();
            revisions.put(newPath, newValue);
            if (tags != null) {
                revisions.get(newPath).add(tags);
            }
        }
        writeRevisionsToFile();
    }

    /**
     * Writes current revisions to a file
     */
    private static void writeRevisionsToFile(){
        WriteData writer = new WriteData();
        writer.writeRevisions(revisions);
    }

    /**
     * Reads file for current revisions
     */
    private static void readRevisionsFromFile(){
        ReadData reader = new ReadData();
        HashMap<String, ArrayList<Tags>> revisionsMap = reader.readRevisions();
        setRevisions(revisionsMap);
    }
}
