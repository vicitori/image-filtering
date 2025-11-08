package com.vicitori;

import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;
import com.vicitori.core.conv.SequentialConvolution;
import com.vicitori.core.conv.*;
import com.vicitori.core.filters.FiltersLibrary;
import org.openjdk.jmh.annotations.*;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ConvolutionBenchmark {

    private BufferedImage testImage;
    private Filter blurFilter;

    @Param({"1", "2", "4", "8", "16", "32"}) // Example values, can be adjusted
    public int blocksCntX;

    @Param({"1", "2", "4", "8", "16", "32"}) // Example values, can be adjusted
    public int blocksCntY;

    @Setup(Level.Trial)
    public void setUp() {
        testImage = createTestImage(8000, 8000); // Larger image for benchmarking
        blurFilter = FiltersLibrary.get("blur");
    }

    @Benchmark
    public void sequentialConvolution() {
        Convolution conv = new SequentialConvolution();
        conv.apply(testImage, blurFilter);
    }

    @Benchmark
    public void pixelConvolution() {
        Convolution conv = new PixelConvolution();
        conv.apply(testImage, blurFilter);
    }

    @Benchmark
    public void columnConvolution() {
        Convolution conv = new ColumnConvolution();
        conv.apply(testImage, blurFilter);
    }

    @Benchmark
    public void gridConvolution() {
        Convolution conv = new GridConvolution(blocksCntX, blocksCntY);
        conv.apply(testImage, blurFilter);
    }

    @Benchmark
    public void rowConvolution() {
        Convolution conv = new RowConvolution();
        conv.apply(testImage, blurFilter);
    }

    private BufferedImage createTestImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random(42);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, rand.nextInt());
            }
        }
        return image;
    }
}
