package com.vicitori.core;

import java.awt.image.BufferedImage;

public interface Convolution {
    BufferedImage apply(BufferedImage image, Filter filter);
}
