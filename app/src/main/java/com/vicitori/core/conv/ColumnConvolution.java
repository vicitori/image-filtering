package com.vicitori.core.conv;

import com.vicitori.core.Convolution;
import com.vicitori.core.Filter;

import java.awt.image.BufferedImage;

public class ColumnConvolution implements Convolution {
    @Override
    public BufferedImage apply(BufferedImage input, Filter filter) {
        throw new UnsupportedOperationException("Sequential convolution not implemented yet.");
    }
}
