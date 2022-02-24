package model;

/** The Directory class represents a directory, and holds a pathName. */
public class Directory {
  private String pathName;

  /**
   * Constructs a Directory with a specified pathName.
   *
   * @param pathName the path name for this Directory
   */
  public Directory(String pathName) {
    this.pathName = pathName;
  }

  /**
   * Returns this Directory's pathName.
   *
   * @return the path name of this Directory.
   */
  public String getPathName() {
    return this.pathName;
  }
}
