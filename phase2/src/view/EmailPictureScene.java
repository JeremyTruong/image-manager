package view;

import controller.EmailManager;
import model.Picture;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The EmailPictureScene class provides a GUI view allowing the user send an email of a selected
 * picture.
 */
class EmailPictureScene {

  private static Picture selectedPicture;

  /**
   * Creates a JavaFX scene (modal style) which will be the front-end to allow users to send an
   * email to an email-address of their choosing that contains the image they have selected.
   *
   * @param incomingPicture the picture that the user has selected to view from ChooseDirectoryScene
   */
  static void emailScene(Picture incomingPicture) {
    Stage secondaryStage = new Stage();
    selectedPicture = incomingPicture;
    VBox root = new VBox();
    HBox header = new HBox();
    header.setPadding(new Insets(10, 180, 10, 180));
    header.setAlignment(Pos.TOP_CENTER);
    Text title = new Text("Email this image");
    header.getChildren().add(title);
    GridPane gridPane = new GridPane();
    gridPane.setVgap(8);
    gridPane.setHgap(8);
    gridPane.setPadding(new Insets(40, 10, 10, 10));
    gridPane.setAlignment(Pos.TOP_CENTER);
    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    hBox.setSpacing(10);
    hBox.setAlignment(Pos.BOTTOM_RIGHT);
    Button btnEmail = new Button("Send");
    Button btnCancel = new Button("Cancel");
    hBox.getChildren().addAll(btnEmail, btnCancel);

    // Text field for inputting the user's name
    TextField nameField = new TextField();
    nameField.setPromptText("Your Name");

    // Text field to enter email address
    TextField emailField = new TextField();
    emailField.setPromptText("Recipient Email (i.e. yourfriend@gmail.com)");

    VBox body = new VBox(10);
    body.setPadding(new Insets(10, 10, 10, 10));
    body.getChildren().addAll(nameField, emailField);
    body.setAlignment(Pos.TOP_CENTER);

    btnEmail.setOnAction(
        (ActionEvent event) -> {
          EmailManager.emailPicture(nameField.getText(), emailField.getText(), selectedPicture);
        });

    btnCancel.setOnAction(
        (ActionEvent event) -> {
          secondaryStage.close();
        });

    root.getChildren().addAll(header, body, hBox);
    Scene scene = new Scene(root, 500, 200);

    secondaryStage.setScene(scene);
    secondaryStage.show();
  }
}
