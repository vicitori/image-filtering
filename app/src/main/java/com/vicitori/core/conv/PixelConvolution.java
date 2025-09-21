package com.vicitori.domain.convolution;

import com.vicitori.domain.filters.FilterProfile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

public class PixelConvolution implements Convolution {
    private float bias;
    private float factor;

    @Override
    public BufferedImage apply(BufferedImage image, FilterProfile filter) {
        float[][] kernel = filter.kernel();
        this.bias = filter.bias();
        this.factor = filter.factor();

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, image.getType());

        int filterHeight = kernel.length;
        int filterWidth = kernel[0].length;

        int filterHalfH = filterHeight / 2;
        int filterHalfW = filterWidth / 2;

        IntStream.range(0, width * height).parallel().forEach(num -> {
                    int x = num % width;
                    int y = num / width;

                    float red = 0, green = 0, blue = 0;
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

                    Color newPixel = new Color(calibrate(red), calibrate(green), calibrate(blue));
                    synchronized (outputImage) {
                        outputImage.setRGB(x, y, newPixel.getRGB());
                    }
                }
        );
        return outputImage;
    }

    private int calibrate(float value) {
        float newValue = value * factor + bias;
        int intValue = Math.round(newValue);
        return Math.max(0, Math.min(255, intValue));
    }
}
