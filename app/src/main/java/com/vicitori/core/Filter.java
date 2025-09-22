package com.vicitori.core;

public record Filter(float[][] kernel, float factor, float bias) {
    public Filter {
        // Defensive copy to prevent external modification
        if (kernel != null) {
            float[][] copy = new float[kernel.length][];
            for (int i = 0; i < kernel.length; i++) {
                copy[i] = kernel[i].clone();
            }
            kernel = copy;
        }
    }
    
    public float[][] kernel() {
        // Return defensive copy
        if (kernel == null) return null;
        float[][] copy = new float[kernel.length][];
        for (int i = 0; i < kernel.length; i++) {
            copy[i] = kernel[i].clone();
        }
        return copy;
    }
}
