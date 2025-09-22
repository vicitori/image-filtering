package com.vicitori.app;

import com.vicitori.io.ImageIO;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import java.util.concurrent.Callable;

@Command(
        name = "ImageFilteringApp",
        description = "Apply convolution filters to an image.",
        mixinStandardHelpOptions = true
)
public class ImageFilteringApp implements Callable<Integer> {

    @Parameters(index = "0", description = "Path to the input image.", paramLabel = "INPUT_PATH")
    private String inputPath;

    @Parameters(index = "1", description = "Filter name: ${COMPLETION-CANDIDATES}", paramLabel = "FILTER_NAME")
    private String filterName;

    @Parameters(index = "2", arity = "0..1",
            description = "Convolution mode (sequential, parallel (row), parallel (column), parallel (pixel), parallel (grid)). Default: sequential",
            paramLabel = "CONVOLUTION_MODE")
    private String convMode;

    @Parameters(index = "3", arity = "0..1",
            description = "Path to the output image. Default: <INPUT_IMAGE_PATH>_filtered.png",
            paramLabel = "OUTPUT_PATH")
    private String outputPath;

    @Option(names = {"-d", "--dir"}, description = "If specified, treat INPUT_PATH as a directory and process all images inside.")
    private boolean directoryMode = false;

    @Option(names = {"-t", "--threads"}, description = "Number of worker threads for directory processing. Default: 4")
    private int workers = 4;

    @Override
    public Integer call() {
        try {
            ImageIO io = ImageIO.create(directoryMode, inputPath, outputPath);
            FilteringEngine imgProcessor = new FilteringEngine(io);
            String savedPath;
            if (directoryMode) {
                savedPath = imgProcessor.process(filterName, convMode, workers);
            } else {
                savedPath = imgProcessor.process(filterName, convMode);
            }
            System.out.printf("Applied filter '%s' with mode '%s'. Saved result to %s%n",
                    filterName, convMode == null ? "sequential" : convMode, savedPath);
            return 0;
        } catch (ProcessingException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        }
    }
}
