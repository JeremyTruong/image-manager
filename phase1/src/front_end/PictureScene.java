package front_end;

import back_end.Picture;
import back_end.PictureManager;
import back_end.Tags;
import back_end.TagManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;


class PictureScene {

    private static ObservableList<String> observableListPictureTags;
    private ObservableList<String> observableAvailableTags;
    private static boolean hasChanged = false; // whether or not a change has been made to observableListPictureTags

    /** Creates a scene that shows the picture the user selected and functionality to view the picture's current
     * tags, add and/or delete a tag, open the picture in OS, and save changes.
     *
     * @param primaryStage a GUI Stage
     * @param picture picture that user selected
     */
    void imageScene(Stage primaryStage, Picture picture) {
        // Based on https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm#CHDCACIE

        primaryStage.setTitle(picture.getOriginalName());
        BorderPane mainPane = new BorderPane(); // main layout
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        BorderPane leftPane = new BorderPane(); // content on the left side of the scene
        leftPane.setTop(topLeftTextField());
        leftPane.setCenter(new Label("Available Tags"));
        leftPane.setBottom(bottomLeftTagList());
        mainPane.setLeft(leftPane);

        BorderPane rightPane = new BorderPane(); // content on the right side on the scene

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(new Label("Image" + System.lineSeparator() + "Tags"), topRightPictureTags(picture));
        rightPane.setTop(hbox);
        rightPane.setCenter(centerRightPicture(picture));
        rightPane.setBottom(bottomRightButtons(primaryStage, picture));
        mainPane.setRight(rightPane);

        primaryStage.setScene(new Scene(mainPane, 800, 500));
        primaryStage.show();
    }

    /** Creates a GridPane in the top left corner of the Scene whose children allow the user to enter a new tag.
     * A message appears below the text field after the user enters a tag.
     *
     * @return a GridPane with a text field and a button.
     */
    private GridPane topLeftTextField() {

        // Based on https://docs.oracle.com/javafx/2/ui_controls/text-field.htm

        Label label = new Label();
        TextField textField = new TextField();
        Button buttonSubmit = new Button("Submit");

        buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String text = textField.getText();
                if ((text != null && !text.isEmpty())) {
                    if (text.contains("@") || text.contains(".") || text.contains("/")) {
                        label.setText("Illegal characters in tag.");
                    } else if (observableListPictureTags.contains(text) && observableAvailableTags.contains(text)) {
                        label.setText("This tag already exists.");
                    } else if (!observableListPictureTags.contains(text) && observableAvailableTags.contains(text)) {
                        label.setText("Tag added to image.");
                        observableListPictureTags.add(text);
                        hasChanged = true;
                    } else if (observableListPictureTags.contains(text) && !observableAvailableTags.contains(text)) {
                        label.setText("Tag added to available tags.");
                        observableAvailableTags.add(text);
                    } else {
                        observableListPictureTags.add(text);
                        observableAvailableTags.add(text);
                        label.setText("Tag added to image and" + System.lineSeparator() + "available tags.");
                        hasChanged = true;
                    }

                }
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(textField, 0, 0);
        textField.setMaxWidth(150);
        textField.setPromptText("Add new tag to image");
        grid.add(buttonSubmit, 1, 0);
        grid.add(label, 0, 1);

        return grid;
    }

    /** A view of available tags. When right-clicking on a cell, a user can add this tag picture's tags or
     * delete this tag from the list. Deleting will not cause it to delete from picture's tags.
     *
     * @return a ListView that lists items of TagManager.getTags()
     */
    private ListView<String> bottomLeftTagList() {
        // Based on http://tutorials.jenkov.com/javafx/listview.html

        observableAvailableTags = FXCollections.observableArrayList(TagManager.getTags()); // observe TagManager.tags
        ListView<String> listView = new ListView<>();
        listView.setItems(observableAvailableTags);

        // Code based on https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu(); // right-click menu

            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Add Tag to Picture \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                if (!observableListPictureTags.contains(item)) {
                    observableListPictureTags.add(item);
                    hasChanged = true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // alert if user tries to add a tag that already exists in observableListPictureTags
                    alert.setHeaderText("Tag Error");
                    alert.setContentText("This image already contains this tag");
                    alert.showAndWait();
                }
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete Tag from Available Tags \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> {
                listView.getItems().remove(cell.getItem());
                TagManager.removeTag(cell.getItem());
            });
            contextMenu.getItems().addAll(editItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
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

    /** A view of picture's current tags on the top right of PictureScene. User has ability to delete a tag
     * from the list.
     *
     * @param picture that user selected
     * @return a ListView of picture's current tags
     */
    private ListView<String> topRightPictureTags(Picture picture) {
        ListView<String> listView = new ListView<>();
        observableListPictureTags = FXCollections.observableList(PictureManager.getPictureTags(picture));
        listView.setItems(observableListPictureTags);
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setMaxHeight(40);

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteTag = new MenuItem();
            contextMenu.getItems().add(deleteTag);

            deleteTag.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));

            deleteTag.setOnAction(event -> {
                listView.getItems().remove(cell.getItem());
                hasChanged = true; // if a tag is removed, a change has been made
            });

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
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


    /** Allows user to see the picture they selected on the right side of PictureScene.
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
            System.out.println("Cannot find file oops");
        }

        stackPane.getChildren().add(imageView);
        return stackPane;
    }

    /** Creates buttons "Tag History", "Open in OS", "Cancel", and "Save" on the bottom right of PictureScene.
     *
     * @param primaryStage a GUI stage
     * @param picture picture that user selected
     * @return an Hbox that contains 4 buttons
     */
    private HBox bottomRightButtons(Stage primaryStage, Picture picture) {
        HBox hbox = new HBox();
        Button btnHistory = new Button("Tag History");
        Button btnOpen = new Button("Open in OS");
        Button btnCancel = new Button("Cancel");
        Button btnSave = new Button("Save");
        hbox.getChildren().addAll(btnHistory, btnOpen, btnCancel, btnSave);
        hbox.setSpacing(10);

        // Based on https://docs.oracle.com/javafx/2/ui_controls/button.htm
        btnHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NamingHistoryScene.namingHistoryScene(new Stage(), picture);
            }
        });

        // Based on https://stackoverflow.com/questions/5824916/how-do-i-open-an-image-in-the-default-image-viewer-using-java-on-windows
        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().open(picture.getLocation());
                } catch (IOException e) {
                    System.out.println("Cannot find file oops");
                }
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (hasChanged)
                    unsavedChangesAlert(primaryStage, picture);
                else {
                    goBack(primaryStage);
                }
            }
        });

        // Based on https://www.mkyong.com/java/how-to-get-current-timestamps-in-java/
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updatePictureAndAllTags(picture);
                goBack(primaryStage);
            }
        });
        return hbox;
    }

    /** Closes PictureScene and goes to chooseDirectoryScene.
     *
     * @param primaryStage a GUI Stage
     */

    private void goBack(Stage primaryStage) {
        ChooseDirectoryScene dirScene = new ChooseDirectoryScene();
        dirScene.DirectoryScene(primaryStage, ChooseDirectoryScene.selectedDirFile);
    }

    /** Updates picture's tags with the new tags the user selected and updates the list of all available tags.
     *
     * @param picture picture that user selected
     */
    private void updatePictureAndAllTags(Picture picture) {
        TagManager.setTags(observableAvailableTags.toArray(new String[observableAvailableTags.size()]));
        Tags tags = new Tags(new Object[0]);
        tags.setTags(observableListPictureTags.toArray(new String[observableListPictureTags.size()]));
        PictureManager.renameWithTags(picture, tags);
    }

    /** Alert that pops when a user tries to cancel when changes have been made to picture's tags.
     *
     * @param primaryStage a GUI stage
     * @param picture picture that user selected
     */
    private void unsavedChangesAlert(Stage primaryStage, Picture picture){
        // Code based on https://stackoverflow.com/questions/36309385/how-to-change-the-text-of-yes-no-buttons-in-javafx-8-alert-dialogs
        ButtonType btnExit = new ButtonType("Don't save changes", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnSaveExit = new ButtonType("Save changes", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.ERROR,"You made some unsaved changes. Are you sure you " +
                "want to exit without saving?", btnSaveExit, btnExit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnSaveExit)
            updatePictureAndAllTags(picture);

        goBack(primaryStage);
    }

    static ObservableList<String> getObservableListPictureTags() {
        return observableListPictureTags;
    }

    static void setHasChanged(boolean value) {hasChanged = value;}
}

