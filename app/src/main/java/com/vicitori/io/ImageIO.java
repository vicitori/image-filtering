package com.vicitori.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageIO {
    private final Path inputPath;
    private final Path outputPath;
    private final String outputFormat;
    public final List<File> inputFiles;
    public final File outputDir;

    public ImageIO(boolean isDirectory, String inputPathStr, String outputPathStr) {
        if (inputPathStr == null) {
            throw new IllegalArgumentException("IOService: INPUT_PATH must not be null. Restart program.");
        }

        if (isDirectory) {
            File inputDir = validateInputDir(inputPathStr);
            this.inputFiles = collectInputFiles(inputDir);

            this.outputDir = prepareOutputDir(inputDir, outputPathStr);
            this.outputFormat = detectOutputFormat(this.inputFiles);

            this.inputPath = null;
            this.outputPath = null;
        } else {
            this.inputPath = Paths.get(inputPathStr);
            PathAndFormat out = resolveOutputPathAndFormat(inputPathStr, outputPathStr, this.inputPath);
            this.outputPath = out.path();
            this.outputFormat = out.format();

            this.inputFiles = null;
            this.outputDir = null;
        }
    }

    private File validateInputDir(String inputPathStr) {
        File inputDir = new File(inputPathStr);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            throw new IllegalArgumentException("Error: " + inputPathStr + " is not a valid directory.");
        }
        return inputDir;
    }


    private List<File> collectInputFiles(File inputDir) {
        List<File> inputFiles = Arrays.stream(Objects.requireNonNull(inputDir.listFiles()))
                .filter(File::isFile)
                .filter(f -> {
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
                })
                .toList();
        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Error: No image files found in " + inputDir.getAbsolutePath());
        }
        return inputFiles;
    }


    private File prepareOutputDir(File inputDir, String outputPathStr) {
        File outputDir = (outputPathStr == null)
                ? new File(inputDir.getParentFile(), inputDir.getName() + "_filtered")
                : new File(outputPathStr);

        if (outputDir.exists() && !outputDir.isDirectory()) {
            throw new IllegalArgumentException("Output path exists but is not a directory: " + outputDir);
        }
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("ImageIO: Failed to create output directory: " + outputDir.getAbsolutePath());
        }
        return outputDir;
    }

    private String detectOutputFormat(List<File> inputFiles) {
        String firstExt = extensionOfFileName(inputFiles.getFirst().getName());
        return (firstExt == null) ? "png" : firstExt.toLowerCase();
    }

    private record PathAndFormat(Path path, String format) {}

    private PathAndFormat resolveOutputPathAndFormat(String inputPathStr, String outputPathStr, Path inputPath) {
        if (outputPathStr == null) {
            String ext = extensionOfFileName(inputPathStr);
            String format = (ext == null) ? "png" : ext.toLowerCase();

            String base = (ext == null) ? inputPathStr : inputPathStr.substring(0, inputPathStr.length() - ext.length() - 1);
            Path directory = inputPath.getParent();
            Path outPath = (directory == null)
                    ? Paths.get(base + "_filtered." + format)
                    : directory.resolve(base + "_filtered." + format);

            return new PathAndFormat(outPath, format);
        } else {
            String ext = extensionOfFileName(outputPathStr);
            String format = (ext == null) ? "png" : ext.toLowerCase();
            return new PathAndFormat(Paths.get(outputPathStr), format);
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

    public File getOutputDir() {
        return this.outputDir;
    }

    public List<File> getInputFiles() {
        return this.inputFiles;
    }
}
