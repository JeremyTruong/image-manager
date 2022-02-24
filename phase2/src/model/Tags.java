package model;

import java.io.Serializable;
import java.util.ArrayList;

/** The Tags class represents a list of String tags. It stores the list as tags. */
public class Tags implements Serializable {
  private ArrayList<String> tags;

  /**
   * Constructs a Tags, where the argument tags is stored in this Tags' tags ArrayList.
   *
   * @param tags a list of tags
   */
  public Tags(ArrayList<String> tags) {
    this.tags = tags;
  }

  /**
   * Returns tags.
   *
   * @return the list of String tags
   */
  public ArrayList<String> getTags() {
    return tags;
  }

  /**
   * Sets tags to the argument tags.
   *
   * @param tags list of String tags to set this tags to
   */
  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  /**
   * Returns a String representation of this Tags.
   *
   * @return String representation of tags
   */
  public String toString() {
    return tags.toString();
  }

  /**
   * Returns the number of String tags in tags.
   *
   * @return number of String tags in tags
   */
  public int size() {
    return tags.size();
  }
}
