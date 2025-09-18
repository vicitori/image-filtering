package com.vicitori.app;

import com.vicitori.application.ImageProcessingException;
import com.vicitori.application.ImageProcessingService;
import com.vicitori.infrastructure.io.IOService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "ImageFilteringApp",
        description = "Apply convolution filters to an image.",
        mixinStandardHelpOptions = true
)
public class App implements Callable<Integer> {

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

    @Override
    public Integer call() throws ImageProcessingException {
        IOService io = new IOService(inputPath, outputPath);
        ImageProcessingService imgProcessor = new ImageProcessingService(io);
        try {
            String savedPath = imgProcessor.process(filterName, convMode);
            System.out.printf("Applied filter '%s' with mode '%s'. Saved result to %s%n",
                    filterName, convMode == null ? "sequential" : convMode, savedPath);
            return 0;
        } catch (ImageProcessingException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        }
    }
}
