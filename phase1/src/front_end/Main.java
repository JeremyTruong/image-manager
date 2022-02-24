package front_end;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;

// src: https://docs.oracle.com/javafx/2/get_started/hello_world.htm
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("pixels.io");
        Button enter = new Button();
        enter.setText("Enter");
        enter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChooseDirectoryScene chooseDirectory = new ChooseDirectoryScene();
                chooseDirectory.DirectoryScene(primaryStage, ChooseDirectoryScene.selectedDirFile);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(enter);
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
}




