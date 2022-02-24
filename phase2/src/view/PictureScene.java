package view;

import model.Picture;
import controller.PictureManager;
import controller.TagManager;
import model.Tags;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

class PictureScene {

  private static ObservableList<String> observableListPictureTags;
  private ObservableList<String> observableAvailableTags;

  /**
   * Creates a scene that shows the picture the user selected and functionality to view the
   * picture's current tags, add and/or delete a tag, open the picture in OS, and save changes.
   *
   * @param primaryStage a GUI Stage
   * @param picture picture that user selected
   */
  void imageScene(Stage primaryStage, Picture picture) {
    // Based on https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm#CHDCACIE

    primaryStage.setTitle(picture.getLocation().getAbsolutePath());
    BorderPane mainPane = new BorderPane(); // main layout
    mainPane.setPadding(new Insets(10, 10, 10, 10));

    BorderPane leftPane = new BorderPane(); // content on the left side of the scene

    VBox tagTextField = new VBox();
    tagTextField.getChildren().addAll(new Label("Add a new tag"), topLeftTextField());
    leftPane.setTop(tagTextField);

    VBox availableTags = new VBox();
    availableTags.getChildren().addAll(new Label("Available Tags"), bottomLeftTagList());
    leftPane.setBottom(new Label("Available Tags"));
    leftPane.setBottom(availableTags);
    mainPane.setLeft(leftPane);

    BorderPane rightPane = new BorderPane(); // content on the right side on the scene

    HBox hbox = new HBox(5);
    hbox.getChildren()
        .addAll(
            new Label("Image" + System.lineSeparator() + "Tags"),
            topRightPictureTags(primaryStage, picture));
    rightPane.setTop(hbox);
    rightPane.setCenter(centerRightPicture(picture));
    rightPane.setBottom(bottomRightButtons(primaryStage, picture));
    mainPane.setRight(rightPane);

    primaryStage.setScene(new Scene(mainPane, 1000, 600));
    primaryStage.show();
  }

  /**
   * Creates a GridPane in the top left corner of the Scene whose children allow the user to enter a
   * new tag. A message appears below the text field after the user enters a tag.
   *
   * @return a GridPane with a text field and a button.
   */
  private GridPane topLeftTextField() {

    // Based on https://docs.oracle.com/javafx/2/ui_controls/text-field.htm

    Label label = new Label();
    TextField textField = new TextField();
    Button buttonSubmit = new Button("Submit");

    buttonSubmit.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            String text = textField.getText();
            if ((text != null && !text.isEmpty())) {
              if (text.contains(PictureManager.TAG_MARKER)
                  || text.contains(".")
                  || text.contains("/")) {
                label.setText("Illegal characters in tag.");
              } else if (observableListPictureTags.contains(text)
                  && observableAvailableTags.contains(text)) {
                label.setText("This tag already exists.");
              } else if (!observableListPictureTags.contains(text)
                  && observableAvailableTags.contains(text)) {
                label.setText("Tag added to image.");
                observableListPictureTags.add(text);
              } else if (observableListPictureTags.contains(text)
                  && !observableAvailableTags.contains(text)) {
                label.setText("Tag added to available tags.");
                observableAvailableTags.add(text);
              } else {
                observableListPictureTags.add(text);
                observableAvailableTags.add(text);
                label.setText(
                    "Tag added to image and" + System.lineSeparator() + "available tags.");
              }
            }
          }
        });

    GridPane grid = new GridPane();
    grid.setHgap(8);
    grid.setVgap(8);
    grid.add(textField, 0, 0);
    textField.setMaxWidth(150);
    grid.add(buttonSubmit, 1, 0);
    grid.add(label, 0, 1);

    return grid;
  }

  /**
   * A view of available tags. When right-clicking on a cell, a user can add this tag picture's tags
   * or delete this tag from the list. Deleting will not cause it to delete from picture's tags.
   *
   * @return a ListView that lists items of TagManager.getTags()
   */
  private ListView<String> bottomLeftTagList() {
    // Based on http://tutorials.jenkov.com/javafx/listview.html

    observableAvailableTags =
        FXCollections.observableArrayList(TagManager.getTags()); // observe TagManager.tags
    ListView<String> listView = new ListView<>();
    listView.setItems(observableAvailableTags);

    // Code based on https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu

    listView.setCellFactory(
        lv -> {
          ListCell<String> cell = new ListCell<>();

          ContextMenu contextMenu = new ContextMenu(); // right-click menu

          MenuItem editItem = new MenuItem();
          editItem
              .textProperty()
              .bind(Bindings.format("Add Tag to Picture \"%s\"", cell.itemProperty()));
          editItem.setOnAction(
              event -> {
                String item = cell.getItem();
                if (!observableListPictureTags.contains(item)) {
                  observableListPictureTags.add(item);
                } else {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  // alert if user tries to add a tag that already exists in
                  // observableListPictureTags
                  alert.setHeaderText("Tag Error");
                  alert.setContentText("This image already contains this tag");
                  alert.showAndWait();
                }
              });
          MenuItem deleteItem = new MenuItem();
          deleteItem
              .textProperty()
              .bind(Bindings.format("Delete Tag from Available Tags \"%s\"", cell.itemProperty()));
          deleteItem.setOnAction(
              event -> {
                String selectedTag = cell.getItem();
                listView.getItems().remove(selectedTag);
                TagManager.removeTag(selectedTag);
              });
          contextMenu.getItems().addAll(editItem, deleteItem);

          cell.textProperty().bind(cell.itemProperty());

          cell.emptyProperty()
              .addListener(
                  (obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                      cell.setContextMenu(null);
                    } else {
                      cell.setContextMenu(contextMenu);
                    }
                  });
          return cell;
        });

    observableAvailableTags.addListener(
        new ListChangeListener<String>() {
          public void onChanged(ListChangeListener.Change<? extends String> c) {
            while (c.next()) {
              TagManager.setTags(
                  observableAvailableTags.toArray(new String[observableAvailableTags.size()]));
            }
          }
        });

    return listView;
  }

  /**
   * A view of picture's current tags on the top right of PictureScene. User has ability to delete a
   * tag from the list.
   *
   * @param picture that user selected
   * @return a ListView of picture's current tags
   */
  private ListView<String> topRightPictureTags(Stage primaryStage, Picture picture) {
    ListView<String> listView = new ListView<>();
    observableListPictureTags =
        FXCollections.observableList(PictureManager.getPictureTags(picture));
    listView.setItems(observableListPictureTags);
    listView.setOrientation(Orientation.HORIZONTAL);
    listView.setMaxHeight(40);

    observableListPictureTags.addListener(
        new ListChangeListener<String>() {
          public void onChanged(ListChangeListener.Change<? extends String> c) {
            while (c.next()) {
              updatePictureWithTags(picture);
              primaryStage.setTitle(picture.getLocation().getAbsolutePath());
            }
          }
        });

    listView.setCellFactory(
        lv -> {
          ListCell<String> cell = new ListCell<>();

          ContextMenu contextMenu = new ContextMenu();
          MenuItem deleteTag = new MenuItem();
          contextMenu.getItems().add(deleteTag);

          deleteTag.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));

          deleteTag.setOnAction(
              event -> {
                listView.getItems().remove(cell.getItem());
              });

          cell.textProperty().bind(cell.itemProperty());

          cell.emptyProperty()
              .addListener(
                  (obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                      cell.setContextMenu(null);
                    } else {
                      cell.setContextMenu(contextMenu);
                    }
                  });

          return cell;
        });

    return listView;
  }

  /**
   * Allows user to see the picture they selected on the right side of PictureScene.
   *
   * @param picture picture that user selected
   * @return a StackPane whose child is an ImageView of picture
   */
  private StackPane centerRightPicture(Picture picture) {
    StackPane stackPane = new StackPane();
    ImageView imageView = new ImageView();

    try {
      FileInputStream input = new FileInputStream(picture.getLocation().toString());
      Image image1 = new Image(input);
      imageView.setImage(image1);
      imageView.setFitHeight(350);
      imageView.setPreserveRatio(true);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    stackPane.getChildren().add(imageView);
    return stackPane;
  }

  /**
   * Creates buttons "Tag History", "Open in OS", "Edit Image", and "Go Back" on the bottom right of
   * PictureScene.
   *
   * @param primaryStage a GUI stage
   * @param picture picture that user selected
   * @return an Hbox that contains 4 buttons
   */
  private HBox bottomRightButtons(Stage primaryStage, Picture picture) {
    HBox hbox = new HBox();
    Button btnHistory = new Button("Tag History");
    Button btnOpenD = new Button("Open Directory in OS");
    Button btnOpenP = new Button("Open Image in OS");
    Button btnEditImage = new Button("Edit Image");
    Button btnEmailImage = new Button("Email Image");
    Button btnExit = new Button("Exit");
    hbox.getChildren().addAll(btnHistory, btnOpenD, btnOpenP, btnEditImage, btnEmailImage, btnExit);
    hbox.setSpacing(10);

    // Based on https://docs.oracle.com/javafx/2/ui_controls/button.htm
    btnHistory.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            NamingHistoryScene.namingHistoryScene(new Stage(), picture);
          }
        });

    btnOpenD.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              // Code based on
              // https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
              if (System.getProperty("os.name").toLowerCase().contains("nux")) { // Check if Linux
                // Code based on
                // https://bb-2017-09.teach.cs.toronto.edu/t/cdf-computers-not-allowing-javafx-to-open-files/1786/4
                Runtime.getRuntime()
                    .exec("xdg-open " + picture.getLocation().getAbsoluteFile().getParentFile());
              }
              Desktop.getDesktop().open(picture.getLocation().getAbsoluteFile().getParentFile());
            } catch (IOException e) {
              System.out.println("Directory not found");
            }
          }
        });

    // Based on
    // https://stackoverflow.com/questions/5824916/how-do-i-open-an-image-in-the-default-image-viewer-using-java-on-windows
    btnOpenP.setOnAction(event -> PictureManager.openPictureInOS(picture));

    btnEditImage.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              BufferedImage bufferedImage = ImageIO.read(picture.getLocation());
              Image image = SwingFXUtils.toFXImage(bufferedImage, null);
              EditingScene newEditingScene = new EditingScene();
              javafx.scene.canvas.Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
              GraphicsContext gc = canvas.getGraphicsContext2D();
              newEditingScene.editingScene(primaryStage, canvas, gc, image);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    btnEmailImage.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            EmailPictureScene.emailScene(picture);
          }
        });

    btnExit.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            goBack(primaryStage);
          }
        });

    return hbox;
  }

  /**
   * Closes PictureScene and goes to chooseDirectoryScene.
   *
   * @param primaryStage a GUI Stage
   */
  private void goBack(Stage primaryStage) {
    ChooseDirectoryScene dirScene = new ChooseDirectoryScene();
    dirScene.DirectoryScene(primaryStage, ChooseDirectoryScene.selectedDirFile);
  }

  /**
   * Updates picture's tags with the new tags the user selected and updates the list of all
   * available tags.
   *
   * @param picture picture that user selected
   */
  private void updatePictureWithTags(Picture picture) {
    Tags tags = new Tags(new ArrayList<>(observableListPictureTags));
    PictureManager.renameWithTags(picture, tags);
  }

  static ObservableList<String> getObservableListPictureTags() {
    return observableListPictureTags;
  }
}
