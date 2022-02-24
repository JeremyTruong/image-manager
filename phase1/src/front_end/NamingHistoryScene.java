package front_end;

import back_end.Picture;
import back_end.RevisionManager;
import back_end.Tags;
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

class NamingHistoryScene {
    /**
     * Creates the scene that lists the tag revisions of a selected Picture.
     *
     * @param primaryStage a GUI stage
     * @param picture picture that user selected
     */
    static void namingHistoryScene(Stage primaryStage, Picture picture) {
        // Based on https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm#CHDCACIE, accessed November 10 2017
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
        ArrayList<Tags> tagRevisions = RevisionManager.getRevisions().get(picture.getLocation().getAbsolutePath());
        // accesses the HashMap stored in RevisionManager

        RadioButton original = new RadioButton("Revert to original name: " + picture.getOriginalName());
        original.setUserData(new Tags(new Object[0]));
        original.setToggleGroup(toggleGroup);

        gridPane.add(original, 0, 0);

        if (tagRevisions != null) { // lists all tag revisions of picture that user can choose to revert to
            // Based on https://docs.oracle.com/javafx/2/ui_controls/radio-button.htm, accessed November 11 2017
            for (int i = 0; i < tagRevisions.size(); i++) {
                RadioButton tagRevision = new RadioButton(picture.getOriginalName() + " " +
                        tagRevisions.get(i).toString());
                tagRevision.setUserData(tagRevisions.get(i));
                tagRevision.setToggleGroup(toggleGroup);

                gridPane.add(tagRevision, 0, i + 1);
            }
        }

        // Based on http://www.java2s.com/Tutorials/Java/JavaFX/0350__JavaFX_ScrollPane.htm, accessed November 11 2017
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

        // Based on https://stackoverflow.com/questions/32424915/how-to-get-selected-radio-button-from-togglegroup,
        // accessed November 11 2017
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (toggleGroup.getSelectedToggle() != null) {
                    RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                    Tags toggleGroupValue = (Tags) selectedRadioButton.getUserData();
                    ObservableList<String> list = PictureScene.getObservableListPictureTags();
                    list.clear();
                    try {
                        list.addAll(toggleGroupValue.getTags()); // changes PictureScene's ObservableListPictureTags
                                                                 // to the selected tags
                    } catch (NullPointerException n){
                        ArrayList<String> empty = new ArrayList<>();
                        list.addAll(empty);
                    }
                    PictureScene.setHasChanged(true);
                    primaryStage.close();
                } else {
                    label.setText("No tags selected");
                }
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
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
