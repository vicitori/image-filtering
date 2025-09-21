package com.vicitori.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageIO {

    private final Path inputPath;
    private final Path outputPath;
    private final String outputFormat;

    public ImageIO(String inputPathStr, String outputPathStr) {
        if (inputPathStr == null) {
            throw new IllegalArgumentException("IOService: INPUT_PATH must not be null. Restart program.");
        }
        this.inputPath = Paths.get(inputPathStr);

        if (outputPathStr == null) {
            String ext = extensionOfFileName(inputPathStr);
            this.outputFormat = (ext == null) ? "png" : ext.toLowerCase();

            String base = (ext == null) ? inputPathStr : inputPathStr.substring(0, inputPathStr.length() - ext.length() - 1);
            Path directory = this.inputPath.getParent();
            this.outputPath = (directory == null)
                    ? Paths.get(base + "_filtered." + this.outputFormat)
                    : directory.resolve(base + "_filtered." + this.outputFormat);
        } else {
            this.outputPath = Paths.get(outputPathStr);
            String ext = extensionOfFileName(outputPathStr);
            this.outputFormat = (ext == null) ? "png" : ext.toLowerCase();
        }
    }

    public BufferedImage getImage() throws IOException {
        if (!Files.exists(inputPath)) {
            throw new IOException("IOService: getImage: Input file does not exist: " + inputPath + ".");
        }
        if (!Files.isReadable(inputPath)) {
            throw new IOException("IOService: getImage: Input file is not readable: " + inputPath + ".");
        }
        BufferedImage image = javax.imageio.ImageIO.read(inputPath.toFile());
        if (image == null) {
            throw new IOException("IOService: getImage: Could not read image (unsupported or corrupted): " + inputPath);
        }
        return image;
    }

    public void writeImage(BufferedImage image) throws IOException {
        if (!javax.imageio.ImageIO.write(image, outputFormat, outputPath.toFile())) {
            throw new IOException("IOService: writeImage: Failed to write image in format "
                    + outputFormat + " to path " + outputPath);
        }
    }

    private static String extensionOfFileName(String name) {
        if (name == null) return null;
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return null;
        return name.substring(dot + 1);
    }

    public Path getOutputPath() {
        return outputPath.getFileName();
    }
}
