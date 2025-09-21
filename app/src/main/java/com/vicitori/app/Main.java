package com.vicitori.app;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ImageFilteringApp()).execute(args);
        System.exit(exitCode);
    }
}
