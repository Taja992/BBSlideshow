package GUI.controller;

import javafx.application.Platform;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AppController {


    public Label redPixelsLbl;
    public Label greenPixelsLbl;
    public Label bluePixelsLbl;
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
    private Map<ImageView, String> imageViewToFilePathMap = new HashMap<>();

    public void initialize() {
    }

    public Map<String, Integer> countRGBPixels(String imagePath) {
        Map<String, Integer> rgbCount = new HashMap<>();
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        try {
            BufferedImage img = ImageIO.read(Paths.get(imagePath).toFile());
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int pixel = img.getRGB(x, y);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;
                    totalRed += red;
                    totalGreen += green;
                    totalBlue += blue;
                    String rgb = red + "," + green + "," + blue;
                    rgbCount.put(rgb, rgbCount.getOrDefault(rgb, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Total Red: " + totalRed + ", Total Green: " + totalGreen + ", Total Blue: " + totalBlue);
        redPixelsLbl.setText("Red: " + totalRed + " Pixels");
        greenPixelsLbl.setText("Green: " + totalGreen + " Pixels");
        bluePixelsLbl.setText("Blue: " + totalBlue + " Pixels");
        return rgbCount;
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
            int startIndex = 0;
            if (selectedImageView != null) {
                //checks what the selected Image is and sets start from there
                startIndex = bottomHbox.getChildren().indexOf(selectedImageView);
            }
            //starting at startIndex it goes through every image
            for (int i = startIndex; i < bottomHbox.getChildren().size(); i++) {
                //if the thread gets broken via pause button or something
                if (Thread.interrupted()) {
                    break;
                }
                Node node = bottomHbox.getChildren().get(i);
                //check if ImageView
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;
                    //gets filepath of original Imageview from the hashmap
                    String filePath = imageViewToFilePathMap.get(imageView);
                    Image originalImage = new Image(filePath);
                    //Platform.runLater() allows us to update the JavaFX thread from outside of it
                    Platform.runLater(() -> imgMain.setImage(originalImage));
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
        Image thumbnailImage = new Image(file.toURI().toString(), 0, 100, true, true);
        ImageView imageView = new ImageView(thumbnailImage);
        imageView.setOnMouseClicked(event -> {
            for (Node node : bottomHbox.getChildren()) {
                if (node instanceof ImageView) {
                    node.setEffect(null);
                }
            }
            selectedImageView = imageView;
            imageView.setEffect(borderGlow);
            new Thread(() -> {
                Image originalImage = new Image(file.toURI().toString());
                javafx.application.Platform.runLater(() -> {
                    setImageDetails(originalImage, file);
                    imgMain.setImage(originalImage);
                });
            }).start();
        });
        //Store the ImageView's filepath into a map to load later for slideshow
        imageViewToFilePathMap.put(imageView, file.toURI().toString());
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
        countRGBPixels(String.valueOf(file));

    }


    private String getFileExtension(File file) {
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex +1);
    }

}
