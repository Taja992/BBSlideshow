package GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class AppController {


    @FXML
    private Label footerResolLbl;
    @FXML
    private Label footerSizeLbl;
    @FXML
    private Label footZoomLbl;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button playBtn;
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
    private Thread slideshowThread;
    private ImageView selectedImageView;

    public void initialize() {

    }

    public void spPrev(ActionEvent actionEvent) {
    }

    public void spNext(ActionEvent actionEvent) {
    }

    public void pauseBtn(ActionEvent actionEvent) {
        if (slideshowThread != null) {
            slideshowThread.interrupt();
        }
    }

    public void playBtn(ActionEvent actionEvent) {
        slideshowThread = new Thread(() -> {
            for (Node node : bottomHbox.getChildren()) {
                if (Thread.interrupted()) {
                    break;
                }
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;
                    javafx.application.Platform.runLater(() -> imgMain.setImage(imageView.getImage()));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        slideshowThread.start();
    }

    public void imgNext(ActionEvent actionEvent) {
    }

    public void imgPrev(ActionEvent actionEvent) {
    }


    public void loadImg(ActionEvent actionEvent) {
        File[] files = getFilesFromDirectory();
        if (files != null){
            bottomHbox.getChildren().clear();
            DropShadow borderGlow = createDropShadow();
            addImagesToHBox(files, borderGlow);
        }
    }

    private File[] getFilesFromDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            return selectedDirectory.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
        }
        return null;
    }

    //for when an image is selected
    private DropShadow createDropShadow() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.CYAN);
        borderGlow.setWidth(50);
        borderGlow.setHeight(50);
        return borderGlow;
    }

    private void addImagesToHBox(File[] files, DropShadow borderGlow) {
        for (File file : files) {
            var imageView = getImageView(file, borderGlow);
            bottomHbox.getChildren().add(imageView);
        }
    }

    private ImageView getImageView(File file, DropShadow borderGlow) {
        Image image = new Image(file.toURI().toString(), 0, 100, true, true);
        ImageView imageView = new ImageView(image);
        //set on mouse click to give a way to select each image
        imageView.setOnMouseClicked(event -> {
            // Remove border from all images
            for (Node node : bottomHbox.getChildren()) {
                if (node instanceof ImageView) {
                    node.setEffect(null);
                }
            }
            selectedImageView = imageView;
            Image originalImage = new Image(file.toURI().toString());
            setImageDetails(originalImage, file);
            // Add border to selected image
            imageView.setEffect(borderGlow);
            Image mainImage = new Image(file.toURI().toString());
            imgMain.setImage(mainImage);
        });
        return imageView;
    }

    private void setImageDetails(Image image, File file) {
        imgWidth.setText((int) image.getWidth() + " Pixels");
        imgHeight.setText((int) image.getHeight() + " Pixels");
        String filetype = getFileExtension(file).toUpperCase();
        imgType.setText(filetype + " Image");
        imgName1.setText(file.getName());
        imgName2.setText(file.getName());
        footerResolLbl.setText((int) image.getWidth() + " x " + (int) image.getHeight() + " Pixels");
        long sizeInBytes = file.length();
        String size;
        if (sizeInBytes < 1024) {
            size = sizeInBytes + " B";
        } else if (sizeInBytes < 1024 * 1024) {
            size = sizeInBytes / 1024 + " KB";
        } else {
            size = sizeInBytes / (1024 * 1024) + " MB";
        }
        imgSize.setText(size);
        footerSizeLbl.setText(size);
    }


    private String getFileExtension(File file) {
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex +1);
    }

}
