package com.vicitori.app;

// Exception for high-level image processing failures
public class ProcessingException extends Exception {
    public ProcessingException(String message) {
        super(message);
    }
}
