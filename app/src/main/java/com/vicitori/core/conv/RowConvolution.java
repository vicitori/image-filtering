package com.vicitori.core.conv;

import com.vicitori.core.AbstractConvolution;
import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RowConvolution extends AbstractConvolution implements Convolution {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public BufferedImage apply(BufferedImage image, Filter filter) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, image.getType());

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            final int fy = y;

            tasks.add(() -> {
                for (int x = 0; x < width; x++) {
                    Color newPixel = applyKernel(image, x, fy, filter);
                    // memory areas of different threads do not overlap
                    outputImage.setRGB(x, fy, newPixel.getRGB());
                }
                return null;
            });
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return outputImage;
    }
}
