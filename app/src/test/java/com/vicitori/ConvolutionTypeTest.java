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

public class ConvolutionTypeTest {
    private static final Random RAND = new Random(42);
    private Filter blurFilter;
    private Filter embossFilter;
    private BufferedImage testImage;

    @BeforeEach
    void setUp() {
        blurFilter = FiltersLibrary.get("blur");
        embossFilter = FiltersLibrary.get("emboss");
        testImage = createTestImage(30, 30);
    }

    @Test
    @DisplayName("SequentialConvolution should process images correctly")
    void testSequentialConvolution() {
        Convolution conv = new SequentialConvolution();
        
        BufferedImage result = conv.apply(testImage, blurFilter);
        
        assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
        assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("RowConvolution should process images correctly")
    void testRowConvolution() {
        Convolution conv = new RowConvolution();
        
        BufferedImage result = conv.apply(testImage, blurFilter);
        
        assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
        assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("ColumnConvolution should process images correctly")
    void testColumnConvolution() {
        Convolution conv = new ColumnConvolution();
        
        BufferedImage result = conv.apply(testImage, blurFilter);
        
        assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
        assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("PixelConvolution should process images correctly")
    void testPixelConvolution() {
        Convolution conv = new PixelConvolution();
        
        BufferedImage result = conv.apply(testImage, blurFilter);
        
        assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
        assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("GridConvolution should process images correctly")
    void testGridConvolution() {
        Convolution conv = new GridConvolution();
        
        BufferedImage result = conv.apply(testImage, blurFilter);
        
        assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
        assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("All convolution types should handle different filters")
    void testConvolutionWithDifferentFilters() {
        Convolution[] convolutions = {
            new SequentialConvolution(),
            new RowConvolution(),
            new ColumnConvolution(),
            new PixelConvolution(),
            new GridConvolution()
        };
        
        Filter[] filters = {blurFilter, embossFilter};
        
        for (Convolution conv : convolutions) {
            for (Filter filter : filters) {
                BufferedImage result = conv.apply(testImage, filter);
                
                assertNotNull(result, conv.getClass().getSimpleName() + " should handle " + 
                    filter.getClass().getSimpleName() + " filter");
                assertEquals(testImage.getWidth(), result.getWidth(), "Width should be preserved");
                assertEquals(testImage.getHeight(), result.getHeight(), "Height should be preserved");
            }
        }
    }

    private BufferedImage createTestImage(int width, int height) {
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
}
