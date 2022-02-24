package controller;

import model.Directory;
import model.Picture;
import model.Tags;

import java.io.File;
import java.util.ArrayList;

/** The DirectoryManager class contains a method for moving an image to a given directory. */
public class DirectoryManager {

  /**
   * Moves a Picture to a different Directory. The picture argument specifies a Picture with a
   * current Directory. The directory argument is the new Directory where the picture should be
   * moved.
   *
   * @param picture the picture to be moved
   * @param directory the directory where picture should be moved
   */
  public static void moveImage(Picture picture, Directory directory) {
    String oldName = picture.getLocation().getAbsolutePath();
    String newName = directory.getPathName() + File.separator + picture.getNameWithExtension();
    File fileToMove = picture.getLocation();
    fileToMove.renameTo(new File(newName));
    Tags tags = new Tags(new ArrayList<>());
    RevisionManager.updateRevisions(oldName, newName, tags);
  }
}
