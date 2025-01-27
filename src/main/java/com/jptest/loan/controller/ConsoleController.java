package com.jptest.loan.controller;

import com.jptest.loan.processor.FileInputProcessor;
import com.jptest.loan.processor.ManualInputProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * {@code ConsoleController} class to handle command line input processing.
 * <p>This class is a Spring component that implements {@code CommandLineRunner}
 * to process command line arguments and initiate either file-based or manual input processing
 * for the vehicle loan calculator application.</p>
 */
@Component
public class ConsoleController implements CommandLineRunner {

    private final FileInputProcessor fileInputProcessor;
    private final ManualInputProcessor manualInputProcessor;

    /**
    /**
     * Constructor for {@code ConsoleController}.
     * <p>It injects {@code FileInputProcessor} and {@code ManualInputProcessor} dependencies
     * to handle different input processing methods.</p>
     *
     * @param fileInputProcessor Processor for handling file inputs for loan data.
     * @param manualInputProcessor Processor for handling manual inputs from the console.
     */
    @Autowired
    public ConsoleController(FileInputProcessor fileInputProcessor, ManualInputProcessor manualInputProcessor) {
        this.fileInputProcessor = fileInputProcessor;
        this.manualInputProcessor = manualInputProcessor;
    }

    /**
     * Runs the console application based on the provided command line arguments.
     * <p>If arguments are provided, it processes the input file specified in the first argument.
     * Otherwise, it initiates manual input processing from the console.</p>
     *
     * @param args Command line arguments. Expects a file path as the first argument if provided.
     *             If no arguments are provided, the application defaults to manual input mode.
     */
    @Override
    public void run(String... args){
        if (args.length > 0) {
            String filePath = args[0];
            fileInputProcessor.processFile(filePath);
        } else {
            manualInputProcessor.processInput();
        }
    }
}
