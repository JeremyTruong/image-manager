package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// save function derived from http://java-buddy.blogspot.ca/2013/04/save-canvas-to-png-file.html
// drawing function derived from https://www.youtube.com/watch?v=gjZQB6BmyK4
class EditingScene {

  private boolean drawMode = true;
  static final int minSceneWidth = 800, minSceneHeight = 500;

  void editingScene(Stage primaryStage, Canvas canvas, GraphicsContext gc, Image img) {
    gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
    StackPane pane = new StackPane();
    Stage secondaryStage = new Stage();
    Scene scene =
        new Scene(
            pane,
            Math.max(minSceneWidth, img.getWidth()),
            Math.max(minSceneHeight, img.getHeight()));

    // Instance creation
    ColorPicker colorPicker = colorPicker(gc);
    Label brushSizeLabel = new Label("1.0");
    Slider brushSizeSlider = brushSizeSlider(gc, brushSizeLabel);

    Button btnDrawMode = new Button("Draw");
    Button btnTextMode = new Button("Add Text");
    Button btnClear = new Button("Clear");
    Button btnSave = new Button("Save");

    TextField textField = new TextField();
    ComboBox<String> comboBoxFonts = comboBoxFonts();
    ComboBox<Integer> comboBoxFontSizes = comboBoxFontSizes();

    // Draw functionality
    drawFuntionality(scene, gc, textField, comboBoxFonts, comboBoxFontSizes);

    // Format gridPane
    GridPane gridPane = new GridPane();
    gridPane.addRow(
        0, colorPicker, brushSizeSlider, brushSizeLabel, btnTextMode, btnClear, btnSave);
    gridPane.setHgap(20);
    gridPane.setAlignment(Pos.TOP_CENTER);
    gridPane.setPadding(new Insets(20, 0, 0, 0));

    pane.getChildren().addAll(canvas, gridPane);
    secondaryStage.setScene(scene);
    secondaryStage.show();

    btnDrawMode.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            drawMode = true;
            gridPane.getChildren().clear();
            gridPane.addRow(
                0, colorPicker, brushSizeSlider, brushSizeLabel, btnTextMode, btnClear, btnSave);
            gridPane.setHgap(20);
            gridPane.setAlignment(Pos.TOP_CENTER);
            gridPane.setPadding(new Insets(20, 0, 0, 0));
          }
        });

    btnTextMode.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            drawMode = false;
            gridPane.getChildren().clear();
            gridPane.setHgap(10);
            gridPane.addRow(
                0,
                textField,
                comboBoxFonts,
                comboBoxFontSizes,
                colorPicker,
                btnDrawMode,
                btnClear,
                btnSave);
          }
        });

    btnClear.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
          }
        });

    btnSave.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            saveToFile(primaryStage, canvas);
          }
        });
  }

  private ColorPicker colorPicker(GraphicsContext gc) {
    ColorPicker colorPicker = new ColorPicker();
    // color picker functionality
    colorPicker.setValue(Color.BLACK);
    colorPicker.setOnAction(
        event -> {
          gc.setStroke(colorPicker.getValue());
          gc.setFill(colorPicker.getValue());
        });
    return colorPicker;
  }

  private Slider brushSizeSlider(GraphicsContext gc, Label label) {
    Slider slider = new Slider();
    // brush size functionality
    slider.setMin(1);
    slider.setMax(100);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider
        .valueProperty()
        .addListener(
            e -> {
              // change brush size and label text
              double brushSizeValue = slider.getValue();
              String brushSizeValueStr = String.format("%.1f", brushSizeValue);
              label.setText(brushSizeValueStr);
              gc.setLineWidth(brushSizeValue);
            });

    // set initial brush color
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(1);

    return slider;
  }

  private void drawFuntionality(
      Scene scene,
      GraphicsContext gc,
      TextField textField,
      ComboBox<String> comboBoxFonts,
      ComboBox<Integer> comboBoxFontSizes) {
    scene.setOnMousePressed(
        e -> {
          if (drawMode) {
            gc.beginPath();
            gc.lineTo(e.getSceneX(), e.getSceneY());
            gc.stroke();
          } else { // user is in 'add text mode'
            gc.setLineWidth(1);
            Font font = new Font(comboBoxFonts.getValue(), comboBoxFontSizes.getValue());
            gc.setFont(font);
            gc.strokeText(textField.getText(), e.getSceneX(), e.getSceneY());
            gc.fillText(textField.getText(), e.getSceneX(), e.getSceneY());
          }
        });

    scene.setOnMouseDragged(
        e -> {
          if (drawMode) {
            gc.lineTo(e.getSceneX(), e.getSceneY());
            gc.stroke();
          }
        });
  }

  private ComboBox<String> comboBoxFonts() {
    List<String> arrayListFonts = Font.getFontNames();
    ObservableList<String> observableList = FXCollections.observableList(arrayListFonts);

    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.setItems(observableList);
    comboBox.setMaxWidth(150);

    comboBox.getSelectionModel().select(12); // select Arial on start-up

    return comboBox;
  }

  private ComboBox<Integer> comboBoxFontSizes() {
    ArrayList<Integer> arrayListFontSizes = new ArrayList<>();
    int count = 2;
    for (int i = 0; i < 44; i++) { // populate ComboBox with even numbers between 2 and 88 inclusive
      arrayListFontSizes.add(count);
      count = count + 2;
    }

    ObservableList<Integer> observableList = FXCollections.observableList(arrayListFontSizes);
    ComboBox<Integer> comboBox = new ComboBox<>();
    comboBox.setItems(observableList);
    comboBox.setMaxWidth(85);

    comboBox.getSelectionModel().select(11); // select 24 on start-up

    return comboBox;
  }

  private void saveToFile(Stage primaryStage, Canvas canvas) {
    FileChooser fileChooser = new FileChooser();

    // filter file types to png
    FileChooser.ExtensionFilter extensionFilter =
        new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
    fileChooser.getExtensionFilters().add(extensionFilter);

    // write to file
    File file = fileChooser.showSaveDialog(primaryStage);
    if (file != null) {
      try {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(renderedImage, "png", file);
      } catch (IOException e) {
        System.out.println("Could not write image to file.");
      }
    }
  }
}
