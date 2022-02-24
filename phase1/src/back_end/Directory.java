package back_end;

/** A class representing a directory */
public class Directory {
    private String pathName;

    /** Constructor for class Directory
     *
     * @param pathName the name of the path for this directory
     */
    public Directory(String pathName) {
        this.pathName = pathName;
    }

    /** Method to return the path name of this directory */
    String getPathName() {
        return this.pathName;
    }
}







