package com.vicitori.core;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractConvolution implements Convolution {
    protected final Color applyKernel(BufferedImage image, int x, int y, Filter filter) {
        float[][] kernel = filter.kernel();
        float factor = filter.factor();
        float bias = filter.bias();

        int filterHeight = kernel.length;
        int filterWidth = kernel[0].length;

        int filterHalfH = filterHeight / 2;
        int filterHalfW = filterWidth / 2;

        int width = image.getWidth();
        int height = image.getHeight();

        float red = 0f, green = 0f, blue = 0f;

        for (int filterY = 0; filterY < filterHeight; filterY++) {
            for (int filterX = 0; filterX < filterWidth; filterX++) {

                int imageX = (x - filterHalfW + filterX + width) % width;
                int imageY = (y - filterHalfH + filterY + height) % height;
                Color pixel = new Color(image.getRGB(imageX, imageY));

                red += pixel.getRed() * kernel[filterY][filterX];
                green += pixel.getGreen() * kernel[filterY][filterX];
                blue += pixel.getBlue() * kernel[filterY][filterX];
            }
        }
        return new Color(calibrate(red, factor, bias), calibrate(green, factor, bias), calibrate(blue, factor, bias));
    }

    protected final int calibrate(float value, float factor, float bias) {
        float newValue = value * factor + bias;
        int intValue = Math.round(newValue);
        return Math.max(0, Math.min(255, intValue));
    }
}
