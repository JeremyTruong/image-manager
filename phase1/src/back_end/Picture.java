package back_end;

import java.io.File;

/** A class to represent a selected Picture or Image */
public class Picture {
    private String originalName;
    private File location;

    public Picture(String originalName, File location) {
        this.originalName = originalName;
        this.location = location;
    }

    /** getter to get the original name of this picture */
    public String getOriginalName() {
        return originalName;
    }

    /** getter to get the file location of this picture */
    public File getLocation() {
        return location;
    }

    /** setter to set the file location of this picture */
    void setLocation(File location) {
        this.location = location;
    }

    /** returns string of the directory the picture is in
     * @return string of the directory the picture is in
     */
    String getDirectoryString() {
        return location.getAbsolutePath()
                .substring(0, location.getAbsolutePath().lastIndexOf(File.separator ) + 1);
    }

    /** returns the name of the picture without any tags
     * @return string of picture without tags
     */
    String getNameWithoutTags() {
        return location.getAbsolutePath().substring(location.getAbsolutePath().lastIndexOf(File.separator ) + 1);
    }
}
