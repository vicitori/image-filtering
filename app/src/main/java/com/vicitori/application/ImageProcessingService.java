package com.vicitori.application;

import com.vicitori.domain.convolution.*;
import com.vicitori.domain.filters.Filters;
import com.vicitori.infrastructure.io.IOService;

import java.awt.image.BufferedImage;

public class ImageProcessingService {
    private final IOService io;

    public ImageProcessingService(IOService io) {
        this.io = io;
    }

    public String process(String filterName, String convMode) throws ImageProcessingException {
        System.out.println(filterName);
        try {
            float[][] kernel = Filters.get(filterName.toLowerCase());
            if (kernel == null) {
                throw new ImageProcessingException("ImageProcessingService: process: Unknown filter: " + filterName + ". Available: " + Filters.names());
            }
            Convolution convolution = createConvolution(convMode);
            BufferedImage image = io.getImage();
            BufferedImage result = convolution.apply(image, kernel);
            io.writeImage(result);
            return io.getOutputPath().toString();
        } catch (Exception e) {
            throw new ImageProcessingException("Processing failed: " + e.getMessage());
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
