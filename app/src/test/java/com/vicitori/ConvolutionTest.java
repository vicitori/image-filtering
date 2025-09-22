package com.vicitori;

import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;
import com.vicitori.core.conv.*;
import com.vicitori.core.filters.FiltersLibrary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.awt.image.BufferedImage;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ConvolutionTest {
    private static final Random RAND = new Random(42);
    private Convolution sequentialConv;
    private Filter blurFilter;
    private Filter embossFilter;
    private Filter edgeFilter;

    @BeforeEach
    void setUp() {
        sequentialConv = new SequentialConvolution();
        blurFilter = FiltersLibrary.get("blur");
        embossFilter = FiltersLibrary.get("emboss");
        edgeFilter = FiltersLibrary.get("find_edges");
    }

    @Test
    @DisplayName("Identity filter should return unchanged image")
    void testIdentityFilter() {
        Filter identity = createIdentityFilter(3);
        BufferedImage testImage = createTestImage(50, 50);
        
        BufferedImage result = sequentialConv.apply(testImage, identity);
        
        assertTrue(imagesEqual(testImage, result), "Identity filter should return unchanged image");
    }

    @Test
    @DisplayName("Zero filter should return black image")
    void testZeroFilter() {
        Filter zero = createZeroFilter(3);
        BufferedImage testImage = createTestImage(50, 50);
        
        BufferedImage result = sequentialConv.apply(testImage, zero);
        
        assertTrue(isBlackImage(result), "Zero filter should return black image");
    }

    @Test
    @DisplayName("Filter composition should work correctly")
    void testFilterComposition() {
        Filter leftShift = createShiftFilter(-1, 0);
        Filter rightShift = createShiftFilter(1, 0);
        BufferedImage testImage = createTestImage(30, 30);
        
        BufferedImage step1 = sequentialConv.apply(testImage, leftShift);
        BufferedImage step2 = sequentialConv.apply(step1, rightShift);
        
        assertTrue(imagesEqual(testImage, step2), "Composition of left and right shift should return original image");
    }

    @Test
    @DisplayName("All convolution types should preserve image dimensions")
    void testImageDimensionsPreserved() {
        Convolution[] convolutions = {
            new SequentialConvolution(),
            new RowConvolution(),
            new ColumnConvolution(),
            new PixelConvolution(),
            new GridConvolution()
        };
        
        BufferedImage testImage = createTestImage(40, 40);
        
        for (Convolution conv : convolutions) {
            BufferedImage result = conv.apply(testImage, blurFilter);
            
            assertEquals(testImage.getWidth(), result.getWidth(), 
                conv.getClass().getSimpleName() + " should preserve width");
            assertEquals(testImage.getHeight(), result.getHeight(), 
                conv.getClass().getSimpleName() + " should preserve height");
        }
    }

    @Test
    @DisplayName("All convolution types should produce equivalent results")
    void testConvolutionEquivalence() {
        Convolution[] convolutions = {
            new SequentialConvolution(),
            new RowConvolution(),
            new ColumnConvolution(),
            new PixelConvolution(),
            new GridConvolution()
        };
        
        Filter[] filters = {blurFilter, embossFilter, edgeFilter};
        BufferedImage testImage = createTestImage(20, 20);
        
        for (Filter filter : filters) {
            BufferedImage reference = sequentialConv.apply(testImage, filter);
            
            for (int i = 1; i < convolutions.length; i++) {
                BufferedImage result = convolutions[i].apply(testImage, filter);
                assertTrue(imagesEqual(reference, result), 
                    convolutions[i].getClass().getSimpleName() + " should produce same result as SequentialConvolution");
            }
        }
    }

    // Вспомогательные методы
    private static Filter createIdentityFilter(int size) {
        float[][] kernel = new float[size][size];
        kernel[size/2][size/2] = 1.0f;
        return new Filter(kernel, 1.0f, 0.0f);
    }

    private static Filter createZeroFilter(int size) {
        float[][] kernel = new float[size][size];
        return new Filter(kernel, 1.0f, 0.0f);
    }

    private static Filter createShiftFilter(int dx, int dy) {
        float[][] kernel = new float[3][3];
        kernel[1+dy][1+dx] = 1.0f;
        return new Filter(kernel, 1.0f, 0.0f);
    }

    private static BufferedImage createTestImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = RAND.nextInt(256);
                int g = RAND.nextInt(256);
                int b = RAND.nextInt(256);
                img.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return img;
    }

    private static boolean imagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isBlackImage(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (img.getRGB(x, y) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
