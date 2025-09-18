package com.vicitori.domain.convolution;

import com.vicitori.domain.filters.FilterProfile;

import java.awt.image.BufferedImage;

public class PixelConvolution implements Convolution {
    @Override
    public BufferedImage apply(BufferedImage input, FilterProfile filter) {
        throw new UnsupportedOperationException("Sequential convolution not implemented yet.");
    }
}
