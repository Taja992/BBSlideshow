package GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;

public class AppController {


    @FXML
    private HBox bottomHbox;
    @FXML
    private ScrollPane bottomSp;
    @FXML
    private VBox mainVbox;
    @FXML
    private Button spPrev;
    @FXML
    private Button spNext;
    @FXML
    private Button imgNext;
    @FXML
    private Button imgPrev;
    @FXML
    private Button playSlide;
    @FXML
    private Button pauseSlide;
    @FXML
    private Button loadImg;
    @FXML
    private Label imgWidth;
    @FXML
    private Label imgName1;
    @FXML
    private Label imgName2;
    @FXML
    private Label imgHeight;
    @FXML
    private Label imgType;
    @FXML
    private Label imgSize;
    @FXML
    private ImageView imgMain;

    public void initialize() {

    }

    public void spPrev(ActionEvent actionEvent) {
    }

    public void spNext(ActionEvent actionEvent) {
    }

    public void imgNext(ActionEvent actionEvent) {
    }

    public void imgPrev(ActionEvent actionEvent) {
    }
    public void pauseSlide(ActionEvent actionEvent) {
    }

    public void playSlide(ActionEvent actionEvent) {
    }

    public void loadImg(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            File[] files = selectedDirectory.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
            if (files != null){
                bottomHbox.getChildren().clear(); {
                    for (File file : files) {
                        Image image = new Image(file.toURI().toString(), 100, 100, true, true);
                        ImageView imageView = new ImageView(image);
                        bottomHbox.getChildren().add(imageView);
                    }
                }
            }
        }

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File(System.getProperty(user.home)));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
//        );
//        File selectedFile = fileChooser.showOpenDialog(null);
//        if (selectedFile != null) {
//            Image image = new Image(selectedFile.toURL().toString());
//            ImageView imageView = new ImageView(image);
//            mainVbox.getChildren().add(imageView);
//        }
    }
}
