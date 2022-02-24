package model;

import java.io.File;

/** The Picture class represents a picture file. It stores an originalName, and a file location. */
public class Picture {
  private String originalName;
  private File location;

  /**
   * Constructs a Picture, where the originalName argument is the original name of this Picture, and
   * location is the File where this Picture is stored.
   *
   * @param originalName original name of this Picture
   * @param location file location of this Picture
   */
  public Picture(String originalName, File location) {
    this.originalName = originalName;
    this.location = location;
  }

  /**
   * Returns the originalName of this Picture.
   *
   * @return original name of this Picture
   */
  public String getOriginalName() {
    return originalName;
  }

  /**
   * Returns the location of this Picture.
   *
   * @return file location of this Picture
   */
  public File getLocation() {
    return location;
  }

  /**
   * Sets this Picture's location to the given location argument.
   *
   * @param location file location this Picture should be set to
   */
  public void setLocation(File location) {
    this.location = location;
  }

  /**
   * Returns a String of the directory of this Picture.
   *
   * @return string of the directory of this Picture
   */
  public String getDirectoryString() {
    return location
        .getAbsolutePath()
        .substring(0, location.getAbsolutePath().lastIndexOf(File.separator) + 1);
  }

  /**
   * Returns the name of this Picture without parent directories, and with the file extension.
   *
   * @return name of this Picture's file
   */
  public String getNameWithExtension() {
    return location
        .getAbsolutePath()
        .substring(location.getAbsolutePath().lastIndexOf(File.separator) + 1);
  }
}
