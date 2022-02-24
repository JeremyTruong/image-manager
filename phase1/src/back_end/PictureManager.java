package back_end;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

/** A class to store all Picture related methods. */
public class PictureManager {

    /**
     * Renames a picture to include the original name with the addition of tags.
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
        for (Object tag: tags.getTags()) {
            tagBuilder.append(" @" + tag.toString());
        }
        String extension = oldName.toString().substring(indexOfLastDot);
        File newName = new File(directoryLocation + tagBuilder + extension);
        oldName.renameTo(newName);
        LogManager.addLog(oldName.toString(), newName.toString());
        picture.setLocation(newName);
        RevisionManager.updateRevisions(oldName.getAbsolutePath(), newName.getAbsolutePath(), tags);
    }

    /**
     * Returns the picture's tags.
     *
     * @param picture picture that contains the tags to be returned
     * @return an ArrayList of String tags that the picture has
     */
    public static ArrayList<String> getPictureTags(Picture picture) {
        String pictureName = picture.getLocation().toString().replace("\\", "/");
        String picNameSubstring = pictureName.substring(pictureName.lastIndexOf("/")+1);

        String[] tagsArray = pictureName.split("@");

        // trim whitespace from Tags
        for (int i = 0; i < tagsArray.length; i++)
            tagsArray[i] = tagsArray[i].trim();

        if (pictureName.contains("@")) {
            ArrayList<String> tempList = new ArrayList<String>(Arrays.asList(tagsArray));
            tempList.remove(0);
            if (tempList.get(tempList.size()-1).contains(".")) {
                String finalTag = tempList.remove(tempList.size()-1);
                tempList.add(finalTag.substring(0, finalTag.indexOf(".")));
            }
            return tempList;

        }
        return new ArrayList<>();

    }

    /**
     * Returns a boolean indicating whether or not a file is a Picture.
     *
     * @param file file that is checked
     * @return boolean indicating whether or not file is a Picture
     */
    public static boolean isPicture(File file){
        boolean valid = true;
        try {
            Image image = ImageIO.read(file);
            if (image == null) {
                valid = false;
            }
        } catch(IOException ex) {
            valid = false;
        }
        return valid;
    }

    /**
     * Returns a String of the originalName of a picture.
     *
     * @param fileName name of the file the Picture is stored as
     * @return a String of the Picture's originalName
     */
    public static String generateOriginalName(String fileName) {
        int indexOfLastDot = fileName.lastIndexOf(".");
        int indexOfFirstAt = fileName.indexOf(" @");

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
}
