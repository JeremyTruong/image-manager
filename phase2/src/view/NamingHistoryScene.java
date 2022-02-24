package view;

import model.Picture;
import controller.RevisionManager;
import model.Tags;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * The NamingHistoryScene class provides a GUI view allowing the user to revert a Picture's name to
 * old revisions.
 */
class NamingHistoryScene {
  /**
   * Creates the scene that lists the tag revisions of a selected Picture. Revisions are viewed on a
   * grid, listing each in chronological order. The first item is the original name of the Picture.
   * When a new name revision occurs for a Picture, the new revision is added to the grid. Only one
   * revision can be selected at a time. The name changes to the selected revision when the button
   * "Revert to Tags" is pressed, and the scene closes. If no revision is selected, the user is
   * notified and the scene stays open. If the button "Cancel" is pressed, the scene closes without
   * saving a new revision.
   *
   * @param primaryStage a GUI stage
   * @param picture picture that user selected
   */
  static void namingHistoryScene(Stage primaryStage, Picture picture) {
    // Based on https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm#CHDCACIE, accessed
    // November 10 2017
    VBox root = new VBox();

    HBox header = new HBox();
    header.setPadding(new Insets(10, 180, 10, 180));
    header.setAlignment(Pos.TOP_CENTER);
    Text title = new Text("Tag Revision History");
    header.getChildren().add(title);

    GridPane gridPane = new GridPane();
    gridPane.setVgap(8);
    gridPane.setHgap(8);
    gridPane.setPadding(new Insets(40, 10, 10, 10));
    gridPane.setAlignment(Pos.TOP_CENTER);

    ToggleGroup toggleGroup = new ToggleGroup();
    ArrayList<Tags> tagRevisions =
        RevisionManager.getRevisions().get(picture.getLocation().getAbsolutePath());
    // accesses the HashMap stored in RevisionManager

    RadioButton original = new RadioButton("Revert to original name: " + picture.getOriginalName());
    original.setUserData(new Tags(new ArrayList<>()));
    original.setToggleGroup(toggleGroup);

    gridPane.add(original, 0, 0);

    if (tagRevisions
        != null) { // lists all tag revisions of picture that user can choose to revert to
      // Based on https://docs.oracle.com/javafx/2/ui_controls/radio-button.htm, accessed November
      // 11 2017
      for (int i = 0; i < tagRevisions.size(); i++) {
        RadioButton tagRevision =
            new RadioButton(picture.getOriginalName() + " " + tagRevisions.get(i).toString());
        tagRevision.setUserData(tagRevisions.get(i));
        tagRevision.setToggleGroup(toggleGroup);

        gridPane.add(tagRevision, 0, i + 1);
      }
    }

    // Based on http://www.java2s.com/Tutorials/Java/JavaFX/0350__JavaFX_ScrollPane.htm, accessed
    // November 11 2017
    ScrollPane scroll = new ScrollPane();
    scroll.setFitToHeight(true);
    scroll.setMinViewportHeight(300);
    scroll.setContent(gridPane);

    Label label = new Label();

    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    hBox.setSpacing(10);
    hBox.setAlignment(Pos.BOTTOM_RIGHT);
    Button btnSelect = new Button("Revert to Tags");
    Button btnCancel = new Button("Cancel");
    hBox.getChildren().addAll(label, btnSelect, btnCancel);

    // Based on
    // https://stackoverflow.com/questions/32424915/how-to-get-selected-radio-button-from-togglegroup,
    // accessed November 11 2017
    btnSelect.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (toggleGroup.getSelectedToggle() != null) {
              RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
              Tags toggleGroupValue = (Tags) selectedRadioButton.getUserData();
              ObservableList<String> list = PictureScene.getObservableListPictureTags();
              // list.clear();
              try {
                list.setAll(
                    toggleGroupValue.getTags()); // changes PictureScene's ObservableListPictureTags
                // to the selected tags
              } catch (NullPointerException n) {
                ArrayList<String> empty = new ArrayList<>();
                list.setAll(empty);
              }
              primaryStage.close();
            } else {
              label.setText("No tags selected");
            }
          }
        });

    btnCancel.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            primaryStage.close();
          }
        });

    root.getChildren().addAll(header, scroll, hBox);
    Scene scene = new Scene(root, 500, 400);

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
