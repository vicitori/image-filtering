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

public class GridConvolution extends AbstractConvolution implements Convolution {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // in the future it will be possible to set via cli
    private final int blocksCntX;
    private final int blocksCntY;

    public GridConvolution(int blocksCntX, int blocksCntY) {
        this.blocksCntX = blocksCntX;
        this.blocksCntY = blocksCntY;
    }

    // now uses these randomly selected values
    public GridConvolution() {
        this.blocksCntX = 10;
        this.blocksCntY = 6;
    }

    @Override
    public BufferedImage apply(BufferedImage image, Filter filter) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, image.getType());

        int blockWidth = (int) Math.ceil((double) width / this.blocksCntX);
        int blockHeight = (int) Math.ceil((double) height / this.blocksCntY);

        List<Callable<Void>> tasks = new ArrayList<>();

        // divide the picture into blocksCntY * blocksCntX of rectangles
        for (int by = 0; by < this.blocksCntY; by++) {
            for (int bx = 0; bx < this.blocksCntX; bx++) {
                final int startX = bx * blockWidth;
                final int startY = by * blockHeight;
                final int endX = Math.min(startX + blockWidth, width);
                final int endY = Math.min(startY + blockHeight, height);

                tasks.add(() -> {
                    for (int y = startY; y < endY; y++) {
                        for (int x = startX; x < endX; x++) {
                            Color newPixel = applyKernel(image, x, y, filter);
                            // memory areas of different threads do not overlap
                            outputImage.setRGB(x, y, newPixel.getRGB());
                        }
                    }
                    return null;
                });
            }
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return outputImage;
    }
}
