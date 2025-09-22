package com.vicitori.app;

import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;
import com.vicitori.core.conv.*;
import com.vicitori.core.filters.FiltersLibrary;
import com.vicitori.io.ImageIO;
import com.vicitori.pipeline.ImagePipeline;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class FilteringEngine {
    private final ImageIO io;

    public FilteringEngine(ImageIO io) {
        this.io = Objects.requireNonNull(io, "ImageIO cannot be null");
    }

    public String process(String filterName, String convMode) throws ProcessingException {
        try {
            Filter filter = FiltersLibrary.get(filterName.toLowerCase());
            if (filter == null) {
                throw new ProcessingException("FilteringEngine: process: Unknown filter: " + filterName + ". Available: " + FiltersLibrary.getNames());
            }
            Convolution convolution = createConvolution(convMode);
            BufferedImage image = io.getImage();
            BufferedImage result = convolution.apply(image, filter);
            io.writeImage(result);
            return io.getOutputPath().toString();
        } catch (Exception e) {
            throw new ProcessingException("FilteringEngine: process: " + e.getMessage());
        }
    }

    public String process(String filterName, String convMode, int workers) throws ProcessingException {
        try {
            Filter filter = FiltersLibrary.get(filterName.toLowerCase());
            if (filter == null) {
                throw new ProcessingException("FilteringEngine: process: with workers: Unknown filter: " + filterName + ". Available: " + FiltersLibrary.getNames());
            }
            Convolution convolution = createConvolution(convMode);
            new ImagePipeline(io.getInputFiles(), io.getOutputDir(), workers, convolution, filter).start();
            return io.getOutputDir().toString();
        } catch (Exception e) {
            throw new ProcessingException("FilteringEngine: process: with workers: " + e.getMessage());
        }
    }

    private Convolution createConvolution(String convMode) {
        if (convMode == null) return new SequentialConvolution();
        return switch (convMode.toLowerCase()) {
            case "row" -> new RowConvolution();
            case "column" -> new ColumnConvolution();
            case "pixel" -> new PixelConvolution();
            case "grid" -> new GridConvolution();
            default -> new SequentialConvolution();
        };
    }
}
