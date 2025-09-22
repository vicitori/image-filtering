package com.vicitori.pipeline;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;
import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;

public class ImagePipeline {
    private final List<File> inputFiles;
    private final File outputDir;
    private final int workersCount;
    private final Convolution convolution;
    private final Filter filter;

    private final BlockingQueue<File> readQueue = new ArrayBlockingQueue<>(10);
    private final BlockingQueue<Result> writeQueue = new ArrayBlockingQueue<>(10);

    private static final File POISON_FILE = new File("POISON");

    private record Result(BufferedImage image, String name) {}

    public ImagePipeline(List<File> inputFiles, File outputDir, int workersCount, Convolution convolution, Filter filter) {
        // defensive copy to prevent external modification
        this.inputFiles = List.copyOf(inputFiles);
        this.outputDir = outputDir;
        this.workersCount = workersCount;
        this.convolution = convolution;
        this.filter = filter;
    }

    public void start() throws InterruptedException {
        Thread reader = createReader();
        Thread writer = createWriter();

        reader.start();
        writer.start();

        try (ExecutorService workers = createWorkers()) {
            reader.join();
            workers.shutdown();
            boolean finished = workers.awaitTermination(1, TimeUnit.HOURS);
            if (!finished) {
                System.err.println("Warning: ImagePipeline: start: workers did not finish within timeout");
                workers.shutdownNow();
            }
        }
        writeQueue.put(new Result(null, null));
        writer.join();
    }

    private Thread createReader() {
        return new Thread(() -> {
            try {
                for (File file : inputFiles) {
                    readQueue.put(file);
                }
                readQueue.put(POISON_FILE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Reader");
    }

    private ExecutorService createWorkers() {
        ExecutorService pool = Executors.newFixedThreadPool(workersCount);
        for (int i = 0; i < workersCount; i++) {
            pool.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        File file = readQueue.take();
                        if (file == POISON_FILE) {
                            readQueue.put(file);
                            break;
                        }
                        BufferedImage img = ImageIO.read(file);
                        BufferedImage result = convolution.apply(img, filter);
                        writeQueue.put(new Result(result, file.getName()));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("Error: ImagePipeline: createWorkers: " + e.getMessage());
                }
            });
        }
        return pool;
    }

    private Thread createWriter() {
        return new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Result res = writeQueue.take();
                    if (res.image == null) break;
                    File outputFile = new File(outputDir, "filtered_" + res.name);
                    ImageIO.write(res.image, "png", outputFile);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Error: ImagePipeline: createWriter: " + e.getMessage());
            }
        }, "Writer");
    }
}
