package view;

import controller.DirectoryManager;
import controller.LogManager;
import controller.PictureManager;
import controller.TagManager;
import model.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/** 
 * The ChooseDirectoryScene class is a front-end view class using JavaFX to view the selected directory in a tree-like
 * view.
 * This was adapted from a post from InternetUnexplorer on Jan-30-2016 to a Stack OverFlow forum here:
  * https://stackoverflow.com/questions/35070310/javafx-representing-directories
  */
class ChooseDirectoryScene {

    static File selectedDirFile;

    /** 
     * Creates a scene where users can select a directory and view it's images and sub-directories.
     *
     * @param primaryStage the main stage of the GUI's Scene
     * @param selectedFile the last selected file by user; logical statements in place to check for null, in the case
     *                     the user has not selected a directory yet
      */
     void DirectoryScene(Stage primaryStage, File selectedFile) {
        BorderPane border = new BorderPane();
        if (selectedFile != null) {
            TreeView treeView = generateTree(primaryStage, selectedFile);
            border.setCenter(treeView);
        }

        border.setPadding(new Insets(10, 10, 10, 10));
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(0, 0, 10, 0));
        Button btnChooseDir = new Button("Choose Directory");
        Button btnLogs = new Button("View Logs");
        Button btnTagEditor = new Button("Manage Tags");
        buttons.getChildren().addAll(btnChooseDir, btnLogs, btnTagEditor);
        btnChooseDir.setOnAction(event -> {
             {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(System.getProperty("user.home")));
                File choice = dc.showDialog(primaryStage);
                 if(choice == null || ! choice.isDirectory()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Could not open directory");
                    alert.setContentText("The file is invalid.");
                    alert.showAndWait();
                } else {
                     selectedDirFile = choice;
                     TreeView treeView = generateTree(primaryStage, choice);
                     border.setCenter(treeView);
                 }
            }
        });

         btnLogs.setOnAction(event -> {
             LogManager logManager = new LogManager();
             logManager.readSerializedArrayList();
             LogScene logStage = new LogScene();
             logStage.logScene(primaryStage);
         });

         btnTagEditor.setOnAction(event -> ManageTagsScene.manageTagScene());

        border.setTop(buttons);
        primaryStage.setScene(new Scene(border, 800, 500));
        primaryStage.setTitle("Folder view");
        primaryStage.show();
    }

    /** 
     * Creates a scene where users can view the images and sub-directories of choice, the selected directory. The menu
     * items available for each image include "Move Image to Other Directory", "Edit Image Tags", and "Open Image in
     * OS".
     *
     * @param primaryStage the main stage of the GUI's Scene
     * @param choice File object which is a directory selected by user to generate a Tree-like view of images and
     *               sub-directories of the selected directory.
     */
    private TreeView generateTree(Stage primaryStage, File choice) {
        TreeView<String> treeView = new TreeView<>();
        TagManager.readTagFromFile();
        treeView.setRoot(getNodesForDirectory(choice));
        TagManager.writeTagToFile();
        // Context Menu for Tree
        ContextMenu contextMenu = new ContextMenu();
        MenuItem moveImage = new MenuItem("Move Image to Other Directory");
        MenuItem tagEditor = new MenuItem("Edit Image Tags");
        MenuItem openImageOS = new MenuItem("Open Image in OS");
        // Binding the context menu to the treem items
        moveImage.disableProperty().bind(Bindings.isEmpty(treeView.getSelectionModel().getSelectedItems()));
        tagEditor.disableProperty().bind(Bindings.isEmpty(treeView.getSelectionModel().getSelectedItems()));
        openImageOS.disableProperty().bind(Bindings.isEmpty(treeView.getSelectionModel().getSelectedItems()));

        tagEditor.setOnAction(event -> {
            handleTagEditorClick(primaryStage, treeView.getSelectionModel().getSelectedItem());
        });

        moveImage.setOnAction(event -> {
            handleMoveImage(primaryStage, treeView.getSelectionModel().getSelectedItem());
        });

        openImageOS.setOnAction(event -> {
            TreeItem clicked = treeView.getSelectionModel().getSelectedItem();
            StringBuilder builder = new StringBuilder();
            buildPath(clicked, builder);
            String absPath = getAbsolutePath();
            File file = new File(absPath + builder.toString());
            String fileName = PictureManager.generateOriginalName(clicked.getValue().toString());
            PictureManager.openPictureInOS(new Picture(fileName, file));
        });


        contextMenu.getItems().addAll(moveImage, tagEditor, openImageOS);
        treeView.setContextMenu(contextMenu);
        return treeView;
    }

    /** 
     * Returns all the Tree sub-items upon selecting a directory. If the File object is a directory, the function is run
     * recursively on the directory.
     * This method was adapted from a post by user JB Nizet on a Stack Overflow forum:
     * https://stackoverflow.com/questions/8787359/how-to-get-all-the-parents-of-the-treeitem
     *
     * @param directory Directory passed in by user or recursively
     * @return Tree sub-items of directory
     */
    private TreeItem<String> getNodesForDirectory(File directory) { //Returns a TreeItem representation of the specified directory
        // Icon made by Smashicons from www.flaticon.com
        TreeItem<String> root = new TreeItem<>(directory.getName(), new ImageView("images" + File.separator + "folder.png"));
        for(File f : directory.listFiles()) {
            if(f.isDirectory()) { //Then we call the function recursively
                root.getChildren().add(getNodesForDirectory(f));
            } else {
                if (PictureManager.isPicture(f)) {
                    // Icon made by Smashicons from www.flaticon.com
                    root.getChildren().add(new TreeItem<>(f.getName(), new ImageView("images" + File.separator +"picture.png")));
                    TagManager.loadTagsFromImage(new Picture(f.getName(), new File(f.getName())));

                }
            }
        }
        return root;
    }

    /** 
     * Calls the PictureScene with the respective clicked picture when a user clicks "Edit Image Tags" menu item.
     *
     * @param primaryStage Main stage of the GUI program
     * @param clicked The MenuContext's TreeItem that the user right-clicked on
     */
    private void handleTagEditorClick(Stage primaryStage, TreeItem clicked) {
        StringBuilder builder = new StringBuilder();
        buildPath(clicked, builder);
        String absPath = getAbsolutePath();
        File file = new File(absPath + builder.toString());
        if (!file.isDirectory()) {
            String fileName = PictureManager.generateOriginalName(clicked.getValue().toString());
            Picture picture = new Picture(fileName, file);
            PictureScene pictureScene = new PictureScene();
            pictureScene.imageScene(primaryStage, picture);
        }
    }

    /**
     * Moves the clicked picture to another selected directory when a user clicks "Move Image to Other Directory" menu
     * item.
     *
     * @param primaryStage Main stage of the GUI program
     * @param clicked The MenuContext's TreeItem that the user right-clicked on
     *
     */
    private void handleMoveImage(Stage primaryStage, TreeItem clicked) {
        StringBuilder builder = new StringBuilder();
        buildPath(clicked, builder);
        String absPath = getAbsolutePath();
        DirectoryChooser dc = new DirectoryChooser();
        if (selectedDirFile != null) {
            dc.setInitialDirectory(selectedDirFile);
        } else {
            dc.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        File selected = dc.showDialog(primaryStage);
        File selectedPicture = new File(absPath + builder.toString());
        if (PictureManager.isPicture(selectedPicture)) {
            Picture picture = new Picture(clicked.toString(), selectedPicture);
            DirectoryManager.moveImage(picture, new Directory(selected.toString()));
            ChooseDirectoryScene ds = new ChooseDirectoryScene();
            ds.DirectoryScene(primaryStage, selectedDirFile);
        }
    }

     /**
      * Returns all the Tree sub-items upon selecting a directory. Checks if the File object is a
     * directory and if it is, recursively runs the function on the directory.
      *
     * @param item Directory passed in by user or recursively
     * @param builder Directory passed in by user or recursively
     */
    private void buildPath(TreeItem item, StringBuilder builder) {
        if (item.getParent() != null) {
            buildPath(item.getParent(), builder);  //build path
            builder.append(File.separator);
        }
        builder.append(item.getValue());
    }

    /** 
     * Returns the absolute path of the selected directory.
     *
     * @return The absolute path of directory the user selects
     */
    private String getAbsolutePath() {
        int lastIndex = selectedDirFile.getAbsolutePath().lastIndexOf(File.separator);
        String absPath;
        if (lastIndex == -1) {
            absPath = "";
        }
        else {
            absPath = selectedDirFile.toString().substring(0, lastIndex + 1);
        }
        return absPath;
    }
}

