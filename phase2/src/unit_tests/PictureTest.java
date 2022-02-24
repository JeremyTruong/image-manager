package unit_tests;

import model.Picture;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PictureTest {
  private Picture picture;
  private String originalName = "file";
  private String originalPathName = "unit_tests" + File.separator + "file.jpg";
  private File location;

  @BeforeEach
  void init() {
    location = new File(originalPathName);
    picture = new Picture(originalName, location);
  }

  @Test
  void testGetOriginalName() {
    String actual = picture.getOriginalName();
    assertEquals(originalName, actual);
  }

  @Test
  void testGetLocation() {
    String actual = picture.getLocation().getPath();
    assertEquals(originalPathName, actual);
  }

  @Test
  void testSetLocation() {
    File newLocation = new File("file.jpg");
    picture.setLocation(newLocation);
    String actual = picture.getLocation().getPath();
    assertEquals("file.jpg", actual);
  }

  @Test
  void testGetDirectoryString() {
    String currentDir =
        System.getProperty("user.dir") + File.separator + "unit_tests" + File.separator;
    String actual = picture.getDirectoryString();
    assertEquals(currentDir, actual);
  }

  @Test
  void testGetNameWithExtensionNoTags() {
    String actual = picture.getNameWithExtension();
    assertEquals("file.jpg", actual);
  }

  @Test
  void testGetNameWithExtensionOneTag() {
    location = new File("unit_tests" + File.separator + "file @A.jpg");
    picture = new Picture(originalName, location);
    String actual = picture.getNameWithExtension();
    assertEquals("file @A.jpg", actual);
  }
}
