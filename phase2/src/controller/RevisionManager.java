package controller;

import model.data.ReadData;
import model.data.WriteData;
import model.Tags;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The RevisionManager class stores a HashMap of all naming revisions made to Pictures, revisions,
 * and handles methods that access the HashMap.
 */
public class RevisionManager implements Serializable {
  private static HashMap<String, ArrayList<Tags>> revisions = new HashMap<>();

  /**
   * Returns revisions, the HashMap containing all naming revisions stored.
   *
   * @return HashMap mapping the path name of Pictures to their naming revisions
   */
  public static HashMap<String, ArrayList<Tags>> getRevisions() {
    readRevisionsFromFile();
    return revisions;
  }

  /**
   * Sets revisions with argument revisionsIn.
   *
   * @param revisionsIn HashMap that revisions is set to
   */
  public static void setRevisions(HashMap<String, ArrayList<Tags>> revisionsIn) {
    revisions = revisionsIn;
  }

  /**
   * Updates revisions of a picture with new tags. The argument oldPath is a key in revisions, and
   * is replaced by the argument newPath, keeping all of the current tag revisions. The argument
   * tags is the newest set of tags to add to revisions.
   *
   * @param oldPath String of file's old location
   * @param newPath String of file's new location
   * @param tags Tags of the newest tag revision
   */
  public static void updateRevisions(String oldPath, String newPath, Tags tags) {
    if (revisions.containsKey(oldPath)) {
      ArrayList<Tags> keepTags = revisions.get(oldPath);
      if (tags != null && tags.size() != 0) {
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

  /** Writes current revisions to a file using WriteData. */
  private static void writeRevisionsToFile() {
    WriteData writer = new WriteData();
    writer.writeRevisions(revisions);
  }

  /**
   * Reads file for current revisions using ReadData. The file's revisions are set to
   * RevisionManager's revisions.
   */
  private static void readRevisionsFromFile() {
    ReadData reader = new ReadData();
    HashMap<String, ArrayList<Tags>> revisionsMap = reader.readRevisions();
    setRevisions(revisionsMap);
  }
}
