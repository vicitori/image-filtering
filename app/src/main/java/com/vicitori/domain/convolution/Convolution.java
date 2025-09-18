package com.vicitori.domain.convolution;

import com.vicitori.domain.filters.FilterProfile;

import java.awt.image.BufferedImage;

public interface Convolution {
    BufferedImage apply(BufferedImage image, FilterProfile filter);
}
