package com.vicitori.domain.filters;

public class FilterProfile {
    private final float[][] kernel;
    private final float factor;
    private final float bias;

    public FilterProfile(float[][] kernel, float factor, float bias) {
        this.kernel = kernel;
        this.factor = factor;
        this.bias = bias;
    }

    public float[][] getKernel() {
        return kernel;
    }

    public float getFactor() {
        return factor;
    }

    public float getBias() {
        return bias;
    }
}
