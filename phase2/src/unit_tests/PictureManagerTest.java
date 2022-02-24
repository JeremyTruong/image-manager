package unit_tests;

import model.Picture;
import controller.PictureManager;
import model.Tags;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/** A class to test the {@link PictureManager} class in the model package */
class PictureManagerTest {

  private String originalPathName = "unit_tests" + File.separator + "file.jpg";
  private String originalName = "file";
  private File file = new File(originalPathName);
  private Picture picture = new Picture(originalName, file.getAbsoluteFile());

  /** Initializes each test case. */
  @BeforeEach
  void init() {
    file = new File(originalPathName);
    picture = new Picture(originalName, file.getAbsoluteFile());
  }

  /** */
  @AfterEach
  void tearDown() {
    File newName = picture.getLocation();
    file = new File(originalPathName);
    newName.renameTo(file);
  }

  /** Tests the method renameWithTags, providing an empty list of tags. */
  @Test
  void testRenameWithTagsNone() {
    Tags tags = new Tags(new ArrayList<>());
    PictureManager.renameWithTags(picture, tags);

    assertEquals("file.jpg", picture.getLocation().getName());
  }

  /** Tests the method renameWithTags, providing a list of one tag. */
  @Test
  void testRenameWithTagsOne() {
    ArrayList<String> newTags = new ArrayList<>();
    newTags.add("A");
    Tags tags = new Tags(newTags);
    PictureManager.renameWithTags(picture, tags);

    assertEquals("file @A.jpg", picture.getLocation().getName());
  }

  /** Tests the method renameWithTags, providing a list of three tags. */
  @Test
  void testRenameWithTagsMultiple() {
    ArrayList<String> newTags = new ArrayList<>();
    newTags.add("A");
    newTags.add("B");
    newTags.add("C");
    Tags tags = new Tags(newTags);
    PictureManager.renameWithTags(picture, tags);

    assertEquals("file @A @B @C.jpg", picture.getLocation().getName());
  }

  @Test
  void testGetPictureTagsNone() {
    Tags tags = new Tags(new ArrayList<>());
    PictureManager.renameWithTags(picture, tags);
    ArrayList<String> actual = PictureManager.getPictureTags(picture);
    ArrayList<String> expected = new ArrayList<>();

    assertEquals(expected, actual);
  }

  @Test
  void testGetPictureTagsOne() {
    ArrayList<String> newTags = new ArrayList<>();
    newTags.add("A");
    Tags tags = new Tags(newTags);
    PictureManager.renameWithTags(picture, tags);
    ArrayList<String> actual = PictureManager.getPictureTags(picture);
    ArrayList<String> expected = new ArrayList<>();
    expected.add("A");

    assertEquals(expected, actual);
  }

  @Test
  void testGetPictureTagsMultiple() {
    ArrayList<String> newTags = new ArrayList<>();
    newTags.add("A");
    newTags.add("B");
    newTags.add("C");
    Tags tags = new Tags(newTags);
    PictureManager.renameWithTags(picture, tags);
    ArrayList<String> actual = PictureManager.getPictureTags(picture);
    ArrayList<String> expected = new ArrayList<>();
    expected.add("A");
    expected.add("B");
    expected.add("C");

    assertEquals(expected, actual);
  }

  @Test
  void testIsPictureTrue() {
    boolean result = PictureManager.isPicture(file);

    assertTrue(result);
  }

  @Test
  void testIsPictureFalse() {
    File textFile = new File("unit_tests" + File.separator + "text.txt");
    boolean result = PictureManager.isPicture(textFile);

    assertFalse(result);
  }

  @Test
  void testGenerateOriginalNameNoTags() {
    String fileName = "file.jpg";
    String actual = PictureManager.generateOriginalName(fileName);
    String expected = "file";

    assertEquals(expected, actual);
  }

  @Test
  void testGenerateOriginalNameOneTag() {
    String fileName = "file @A.jpg";
    String actual = PictureManager.generateOriginalName(fileName);
    String expected = "file";

    assertEquals(expected, actual);
  }

  @Test
  void testGenerateOriginalNameMultipleTags() {
    String fileName = "file @A @B @C.jpg";
    String actual = PictureManager.generateOriginalName(fileName);
    String expected = "file";

    assertEquals(expected, actual);
  }
}
