package front_end;

import back_end.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

class LogScene {

    void logScene(Stage primaryStage) {
        primaryStage.setTitle("Naming Logs");
        BorderPane mainPane = new BorderPane(); // main layout
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        Button backButton = new Button("Back");

        backButton.setOnAction((ActionEvent event) -> {
            ChooseDirectoryScene dirScene = new ChooseDirectoryScene();
            dirScene.DirectoryScene(primaryStage, ChooseDirectoryScene.selectedDirFile);
        });
        mainPane.setTop(backButton);
        mainPane.setCenter(generateListView());
        primaryStage.setScene(new Scene(mainPane, 800, 500));
        primaryStage.show();
    }

    private ListView generateListView() {
        ListView listView = new ListView();
        ArrayList<String[]> logs = LogManager.allLogs;
        for (String[] log: logs) {
            StringBuilder sb = new StringBuilder();
            sb.append("Previous Name: " + log[0] + System.lineSeparator());
            sb.append("New Name: " + log[1] + System.lineSeparator());
            sb.append("Timestamp: " + log[2]);
            listView.getItems().add((sb.toString()));
        }
        return listView;
    }
}
