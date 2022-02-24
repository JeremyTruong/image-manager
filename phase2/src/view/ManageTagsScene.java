package view;

import controller.TagManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The ManageTagsScene class provides a GUI view allowing the user to access all the tags used in
 * the application.
 */
public class ManageTagsScene {

  /**
   * Creates a scene that lists all the tags used in the application, accessed from TagManager. The
   * user can delete existing tags and add new unique tags to the list. The scene closes upon
   * pressing the button "Exit".
   */
  static void manageTagScene() {
    Stage secondaryStage = new Stage();
    ObservableList<String> observableTags = FXCollections.observableArrayList(TagManager.getTags());
    ListView<String> listView = new ListView<>();
    listView.setItems(observableTags);

    VBox root = new VBox();
    HBox header = new HBox();
    header.setAlignment(Pos.TOP_CENTER);
    GridPane gridPane = new GridPane();
    gridPane.setVgap(8);
    gridPane.setHgap(8);
    gridPane.setPadding(new Insets(40, 10, 10, 10));
    gridPane.setAlignment(Pos.TOP_CENTER);
    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    hBox.setSpacing(10);
    hBox.setAlignment(Pos.BOTTOM_RIGHT);

    listView.setCellFactory(
        lv -> {
          ListCell<String> cell = new ListCell<>();

          ContextMenu contextMenu = new ContextMenu(); // right-click menu
          MenuItem deleteItem = new MenuItem();
          deleteItem.textProperty().bind(Bindings.format("Delete Tag \"%s\"", cell.itemProperty()));
          deleteItem.setOnAction(
              event -> {
                String selectedTag = cell.getItem();
                listView.getItems().remove(selectedTag);
                TagManager.removeTag(selectedTag);
              });
          contextMenu.getItems().addAll(deleteItem);

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

    Button btnCancel = new Button("Exit");
    hBox.getChildren().addAll(btnCancel);

    // Text field for inputting the user's name
    Label addLabel = new Label("Add a new tag:");
    addLabel.setPadding(new Insets(5, 0, 0, 0));
    TextField addTagField = new TextField();
    HBox addLabelBox = new HBox(10);
    addLabelBox.setPadding(new Insets(10, 10, 0, 0));
    Button btnAddTag = new Button("Add");
    addLabelBox.getChildren().addAll(addLabel, addTagField, btnAddTag);
    header.getChildren().add(addLabelBox);

    btnAddTag.setOnAction(
        event -> {
          String tag = addTagField.getText();
          if (!observableTags.contains(tag)) {
            observableTags.add(tag);
            TagManager.addTag(tag);
          } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert if user tries to add a tag that already exists in observableListPictureTags
            alert.setHeaderText("Tag Error");
            alert.setContentText("This tag already exists");
            alert.showAndWait();
          }
        });

    VBox body = new VBox(10);
    body.setPadding(new Insets(10, 10, 0, 10));
    body.getChildren().addAll(listView);
    body.setAlignment(Pos.TOP_CENTER);

    btnCancel.setOnAction(
        event -> {
          secondaryStage.close();
        });

    root.getChildren().addAll(header, body, hBox);
    Scene scene = new Scene(root, 400, 500);

    secondaryStage.setScene(scene);
    secondaryStage.show();
  }
}
