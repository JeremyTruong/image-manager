package controller;

import model.data.ReadData;
import model.data.WriteData;
import model.Picture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The TagManager class stores tags, a HashSet of all the tags used in the application, and handles
 * methods that access the HashSet.
 */
public class TagManager {
  private static HashSet<String> tags = new HashSet<>(); // stores all tags used

  /**
   * Returns tags.
   *
   * @return a list of all tags
   */
  public static HashSet<String> getTags() {
    readTagFromFile();
    return tags;
  }

  /**
   * Sets tags with the argument newTags.
   *
   * @param newTags new tags to set the list of all tags to
   */
  public static void setTags(String[] newTags) {
    tags = new HashSet<>(Arrays.asList(newTags));
  }

  /**
   * Removes the argument tag from tags. Writes the new revision to a file.
   *
   * @param tag String tag to be removed
   */
  public static void removeTag(String tag) {
    tags.remove(tag);
    writeTagToFile();
  }

  /** Writes tags to a file using WriteData. */
  public static void writeTagToFile() {
    WriteData writer = new WriteData();
    writer.writeTags(tags);
  }

  /** Reads tags from a file using ReadData. */
  public static void readTagFromFile() {
    ReadData reader = new ReadData();
    reader.readTags();
  }

  /**
   * Adds the tags from the argument picture to the list of all tags. The tags are taken from the
   * picture's file name, and duplicate tags are not added.
   *
   * @param picture the Picture that tags are loaded from
   */
  public static void loadTagsFromImage(Picture picture) {
    ArrayList<String> tagsFromPicture = PictureManager.getPictureTags(picture);
    tags.addAll(tagsFromPicture);
  }

  /**
   * Adds the argument tag, a single tag String, to tags. Writes the revision to a file.
   *
   * @param tag a tag to add to tags
   */
  public static void addTag(String tag) {
    tags.add(tag);
    writeTagToFile();
  }
}
