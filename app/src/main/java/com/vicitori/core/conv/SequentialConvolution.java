package com.vicitori.core.conv;

import com.vicitori.core.AbstractConvolution;
import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SequentialConvolution extends AbstractConvolution implements Convolution {
    @Override
    public BufferedImage apply(BufferedImage image, Filter filter) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, image.getType());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color newPixel = applyKernel(image, x, y, filter);
                outputImage.setRGB(x, y, newPixel.getRGB());
            }
        }
        return outputImage;
    }
}
