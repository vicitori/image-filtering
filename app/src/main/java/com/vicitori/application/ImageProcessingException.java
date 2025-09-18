package com.vicitori.application;

// Exception for high-level image processing failures
public class ImageProcessingException extends Exception {
    public ImageProcessingException(String message) {
        super(message);
    }
}
