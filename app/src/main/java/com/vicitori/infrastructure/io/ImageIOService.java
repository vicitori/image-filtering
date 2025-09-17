package main.java.com.vicitori.infrastructure.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIOService {
    public BufferedImage read(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Error: ImageIOService: read: File doesn't exist: " + path + ".");
        }
        return ImageIO.read(file);
    }

    public void write(BufferedImage image, String path, String format) throws IOException {
        File outputFile = new File(path);

        if (!ImageIO.write(image, format, outputFile)) {
            throw new IOException("Error: ImageIOService: write: Failed to write image: " + path + "." + format);
        }
    }
}
