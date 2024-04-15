package GUI.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageProcessing {

    public BufferedImage loadImage(String imagePath) throws IOException {
        return ImageIO.read(Paths.get(imagePath).toFile());
    }

    //    public void countRGBPixels(String imagePath) {
//        Map<String, Integer> rgbCount = new ConcurrentHashMap<>();
//        AtomicInteger totalRed = new AtomicInteger();
//        AtomicInteger totalGreen = new AtomicInteger();
//        AtomicInteger totalBlue = new AtomicInteger();
//        try {
//            BufferedImage img = ImageIO.read(Paths.get(imagePath).toFile());
//            ExecutorService executor = Executors.newFixedThreadPool(4); // adjust to your number of cores
//            List<Future<?>> futures = new ArrayList<>();
//            for (int y = 0; y < img.getHeight(); y++) {
//                for (int x = 0; x < img.getWidth(); x++) {
//                    int finalX = x;
//                    int finalY = y;
//                    futures.add(executor.submit(() -> {
//                        int pixel = img.getRGB(finalX, finalY);
//                        int red = (pixel >> 16) & 0xff;
//                        int green = (pixel >> 8) & 0xff;
//                        int blue = (pixel) & 0xff;
//                        totalRed.addAndGet(red);
//                        totalGreen.addAndGet(green);
//                        totalBlue.addAndGet(blue);
//                        String rgb = red + "," + green + "," + blue;
//                        rgbCount.put(rgb, rgbCount.getOrDefault(rgb, 0) + 1);
//                    }));
//                }
//            }
//            for (Future<?> future : futures) {
//                future.get(); // wait for all tasks to complete
//            }
//            executor.shutdown();
//        } catch (IOException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        redPixelsLbl.setText("Red: " + totalRed.get() + " Pixels");
//        greenPixelsLbl.setText("Green: " + totalGreen.get() + " Pixels");
//        bluePixelsLbl.setText("Blue: " + totalBlue.get() + " Pixels");
//    }

    public Map<String, Integer> countRGBPixels(BufferedImage img) {
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                totalRed += red;
                totalGreen += green;
                totalBlue += blue;
            }
        }
        Map<String, Integer> colorCount = new HashMap<>();
        colorCount.put("Red", totalRed);
        colorCount.put("Green", totalGreen);
        colorCount.put("Blue", totalBlue);
        return colorCount;
    }

    private String getFileExtension(File file) {
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex +1);
    }
}
