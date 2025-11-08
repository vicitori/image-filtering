### Image Filtering Application

A Java application for applying various image filters using different convolution algorithms.

---

### Project Structure

```
app/src/main/java/com/vicitori/
├── app/
│   ├── Main.java                    # Application entry point
│   ├── ImageFilteringApp.java       # CLI interface using Picocli
│   ├── FilteringEngine.java         # Main processing engine
│ 
├── core/
│   ├── Filter.java                  # Filter record with kernel, factor, bias
│   ├── Convolution.java             # Abstract convolution interface
│   ├── AbstractConvolution.java     # Base convolution implementation
│   └── conv/
│       ├── SequentialConvolution.java  
│       ├── RowConvolution.java          
│       ├── ColumnConvolution.java      
│       ├── PixelConvolution.java       
│       └── GridConvolution.java        
├── filters/
│   └── FiltersLibrary.java         
├── io/
│   └── ImageIO.java          
└── pipeline/
    └── ImagePipeline.java           # Multi-threaded batch processing
```

### How to Run

#### Prerequisites
- Download the latest `image-filtering-all.jar` from the Releases page
- Java 21 or higher

#### Single Image Processing
```bash
java -jar image-filtering-all.jar <input_path> <filter_name> [convolution_mode] [output_path]
```

#### Batch Processing (Images in directory)
```bash
java -jar image-filtering-all.jar -d <input_directory> <filter_name> [convolution_mode] [output_directory] [-t threads]
```

### Command Line Options
- `-d, --directory` - Enable directory mode for batch processing
- `-t, --threads` - Number of worker threads (default: 4)
- `-h, --help` - Show help message

### Available Filters

- `blur` - gaussian blur
- `emboss` - emboss effect 
- `find_edges` - edge detection
- `glass_distortion` - glass distortion effect
- `motion_blur` - motion blur 
- `negative` - negative/invert colors
- `pixelate` - pixelation effect
- `radial_blur` - radial blur effect

### Convolution Modes

- `sequential` (default) - Standard sequential processing
- `row` - Row-based parallel processing
- `column` - Column-based parallel processing
- `pixel` - Pixel-by-pixel processing
- `grid` - Grid-based parallel processing

### Usage

```bash
# Apply blur filter to single image
java -jar image-filtering-all.jar input.jpg blur output.jpg

# Apply emboss filter with row convolution
java -jar image-filtering-all.jar input.jpg emboss row output.jpg

# Apply edge detection filter
java -jar image-filtering-all.jar input.jpg find_edges output.jpg

# Apply negative effect
java -jar image-filtering-all.jar input.jpg negative output.jpg

# Apply motion blur effect
java -jar image-filtering-all.jar input.jpg motion_blur output.jpg

# Batch process directory with 4 threads
java -jar image-filtering-all.jar -d ./images blur sequential ./output -t 4
```

### Building

```bash
./gradlew build
./gradlew fatJar 
# image-filtering-all.jar will be at app/build/libs
```

### Benchmarks

Detailed benchmark results, including all measurements, can be found [here](app/jmh-results-detailed.txt).

Here are the benchmark results for different convolution methods using an 8000x8000 pixel image:

```
Benchmark                                   Mode  Cnt      Score      Error  Units
ConvolutionBenchmark.columnConvolution      avgt    5   8577.580 ± 9713.785  ms/op
ConvolutionBenchmark.gridConvolution        avgt    5   5610.405 ±  581.765  ms/op
ConvolutionBenchmark.rowConvolution         avgt    5   5329.240 ±  589.128  ms/op
ConvolutionBenchmark.sequentialConvolution  avgt    5  25006.941 ± 1621.451  ms/op
```

**Conclusion:**

Based on these results, `gridConvolution`, `rowConvolution`, and `columnConvolution` are significantly faster than `sequentialConvolution`. This performance difference is primarily due to their parallel implementations. The `sequentialConvolution` processes the image in a single thread, making it the slowest for large images. In contrast, `gridConvolution`, `rowConvolution`, and `columnConvolution` effectively utilize multi-threading by dividing the image into smaller, independent tasks (grids, rows, or columns), allowing for concurrent processing and better performance on multi-core systems.

Among the parallel implementations, `gridConvolution` is generally the fastest. This is because it divides the image into smaller, more manageable rectangular blocks (grids), which can be processed independently and concurrently by different threads. This approach minimizes contention and maximizes CPU utilization, leading to superior performance.

`RowConvolution` is typically faster than `ColumnConvolution`. In `RowConvolution`, each thread processes an entire row or a set of rows. This can be efficient as data access is often contiguous within a row, which benefits from CPU cache locality.

`ColumnConvolution` is often the slowest among the parallel implementations. This is because processing columns often leads to non-contiguous memory access patterns. When a thread processes a column, it jumps across different memory locations to access pixels in that column. This scattered access pattern can result in frequent cache misses, forcing the CPU to fetch data from slower main memory, thereby reducing performance.

