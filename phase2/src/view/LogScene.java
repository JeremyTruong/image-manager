package view;

import controller.LogManager;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/** The LogScene class provides a GUI view of the logs of all Picture renaming. */
class LogScene {

  /**
   * Creates a scene to list the logs of all Picture renaming.
   *
   * @param primaryStage a GUI stage
   */
  void logScene(Stage primaryStage) {
    primaryStage.setTitle("Naming Logs");
    BorderPane mainPane = new BorderPane(); // main layout
    mainPane.setPadding(new Insets(10, 10, 10, 10));
    Button backButton = new Button("Back");

    backButton.setOnAction(
        (ActionEvent event) -> {
          ChooseDirectoryScene dirScene = new ChooseDirectoryScene();
          dirScene.DirectoryScene(primaryStage, ChooseDirectoryScene.selectedDirFile);
        });
    mainPane.setTop(backButton);
    mainPane.setCenter(generateListView());
    primaryStage.setScene(new Scene(mainPane, 800, 500));
    primaryStage.show();
  }

  /**
   * Returns a log of all Picture renaming taken from LogManager. The log contains the name before
   * the revision, the name after the revision, and a time stamp of when the renaming occurred.
   *
   * @return ListView of all Picture renaming
   */
  private ListView generateListView() {
    ListView listView = new ListView();
    ArrayList<String[]> logs = LogManager.getAllLogs();
    for (String[] log : logs) {
      StringBuilder sb = new StringBuilder();
      sb.append("Previous Name: " + log[0] + System.lineSeparator());
      sb.append("New Name: " + log[1] + System.lineSeparator());
      sb.append("Timestamp: " + log[2]);
      listView.getItems().add((sb.toString()));
    }
    return listView;
  }
}
