package controller;

import model.Picture;
import model.Tags;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

/** The PictureManager class contains methods for handling Pictures. */
public class PictureManager {
  public static final String TAG_MARKER = "@";
  private static final String TAG_ON_FILE = " " + TAG_MARKER;

  /**
   * Renames a picture to include the original name with the addition of tags. The picture argument
   * is the Picture to be renamed. The tags argument holds the tags that the picture's new name
   * should include. Mention of renaming is stored as a log using LogManager, and the new name is
   * updated to the list of naming revisions using RevisionManager.
   *
   * @param picture picture that is renamed
   * @param tags tags to rename the picture
   */
  public static void renameWithTags(Picture picture, Tags tags) {
    File oldName = picture.getLocation();
    String directoryLocation = picture.getDirectoryString();
    int indexOfLastDot = oldName.getAbsolutePath().lastIndexOf(".");
    StringBuilder tagBuilder = new StringBuilder();
    tagBuilder.append(picture.getOriginalName());
    for (Object tag : tags.getTags()) {
      tagBuilder.append(TAG_ON_FILE);
      tagBuilder.append(tag.toString());
    }
    String extension = oldName.toString().substring(indexOfLastDot);
    File newName = new File(directoryLocation + tagBuilder + extension);
    oldName.renameTo(newName);
    LogManager.addLog(oldName.toString(), newName.toString());
    picture.setLocation(newName);
    RevisionManager.updateRevisions(oldName.getAbsolutePath(), newName.getAbsolutePath(), tags);
  }

  /**
   * Returns the tags in a Picture. The picture argument is the Picture containing tags to be
   * returned.
   *
   * @param picture picture that contains the tags to be returned
   * @return tags that the picture has
   */
  public static ArrayList<String> getPictureTags(Picture picture) {
    String pictureName = picture.getLocation().toString().replace("\\", "/");
    String[] tagsArray = pictureName.split(TAG_MARKER);

    // trim whitespace from Tags
    for (int i = 0; i < tagsArray.length; i++) tagsArray[i] = tagsArray[i].trim();

    if (pictureName.contains(TAG_MARKER)) {
      ArrayList<String> tempList = new ArrayList<String>(Arrays.asList(tagsArray));
      tempList.remove(0);
      if (tempList.get(tempList.size() - 1).contains(".")) {
        String finalTag = tempList.remove(tempList.size() - 1);
        tempList.add(finalTag.substring(0, finalTag.indexOf(".")));
      }
      return tempList;
    }
    return new ArrayList<>();
  }

  /**
   * Returns a boolean indicating whether or not a file is a Picture by checking if it is an Image.
   * The file argument is the file that is checked.
   *
   * @param file file that is checked
   * @return boolean indicating whether or not file is a Picture
   */
  public static boolean isPicture(File file) {
    boolean valid = true;
    try {
      Image image = ImageIO.read(file);
      if (image == null) {
        valid = false;
      }
    } catch (IOException ex) {
      valid = false;
    }
    return valid;
  }

  /**
   * Returns a String of the originalName of a picture with no parent directories, tags, or
   * extensions. The fileName argument is a String of the file name of the Picture.
   *
   * @param fileName name of the file the Picture is stored as
   * @return a String of the Picture's originalName
   */
  public static String generateOriginalName(String fileName) {
    int indexOfLastDot = fileName.lastIndexOf(".");
    int indexOfFirstAt = fileName.indexOf(TAG_ON_FILE);

    if (indexOfLastDot != -1) {
      fileName = fileName.substring(0, indexOfLastDot);
      if (indexOfFirstAt != -1) {
        fileName = fileName.substring(0, indexOfFirstAt);
      }
    } else {
      if (indexOfFirstAt != -1) {
        fileName = fileName.substring(0, indexOfFirstAt);
      }
    }
    return fileName;
  }

  /**
   * Opens picture in the OS. The picture argument is the Picture that is opened. Handles
   * IOException if the picture file does not exist.
   *
   * @param picture picture that is selected to be opened in the OS
   */
  public static void openPictureInOS(Picture picture) {
    try {
      // Code based on https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
      if (System.getProperty("os.name").toLowerCase().contains("nux")) { // Check if Linux
        // Code based on
        // https://bb-2017-09.teach.cs.toronto.edu/t/cdf-computers-not-allowing-javafx-to-open-files/1786/4
        Runtime.getRuntime().exec("xdg-open " + picture.getLocation());
      }
      Desktop.getDesktop().open(picture.getLocation());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
