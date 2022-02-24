package back_end;

import java.io.File;


/** A class to store all directory related methods */
public class DirectoryManager {

    /** Recursive function to get all the Tree sub-items upon selecting a directory, checks if the File object is a
     * directory if it is, recursively runs the function of the directory
     *
     * @param picture picture that user selected
     * @param directory the directory the user wishes to move the picture to
     */
    public static void moveImage(Picture picture, Directory directory){
        String oldName = picture.getLocation().getAbsolutePath();
        String newName = directory.getPathName() + File.separator + picture.getNameWithoutTags();
        File fileToMove = picture.getLocation();
        fileToMove.renameTo(new File(newName));
        Tags tags = new Tags(new Object[0]);
        tags.setTags(new String[0]);
        RevisionManager.updateRevisions(oldName, newName, tags);
    }
}
