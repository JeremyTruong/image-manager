package back_end;

import data.ReadData;
import data.WriteData;
import java.util.ArrayList;
import java.util.Arrays;

/** A class to handle the list of all tags. */
public class TagManager {
    private static ArrayList<String> tags = new ArrayList<>(); // stores all tags used

    /**
     * Returns the list of all tags.
     *
     * @return an ArrayList<String> of all tags
     */
    public static ArrayList<String> getTags() {
        readTagFromFile();
        return tags;
    }

    /**
     * Sets the list of all tags with newTags.
     *
     * @param newTags a String[] of tags to set the list of tags to
     */
    public static void setTags(String[] newTags) {
        tags = new ArrayList<>(Arrays.asList(newTags));
        writeTagToFile(tags);
    }

    /**
     * Removes a tag from tags.
     *
     * @param tag String tag to be deleted
     */
    public static void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Writes tags to a file.
     *
     * @param tags ArrayList of String tags
     */
    private static void writeTagToFile(ArrayList<String> tags) {
        WriteData writer = new WriteData();
        writer.writeTags(tags);
    }

    /**
     * Reads tags from a file.
     */
    private static void readTagFromFile() {
        ReadData reader = new ReadData();
        reader.readTags();
    }
}
