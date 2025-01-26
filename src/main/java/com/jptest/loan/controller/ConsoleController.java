package com.jptest.loan.controller;

import com.jptest.loan.processor.FileInputProcessor;
import com.jptest.loan.processor.ManualInputProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleController implements CommandLineRunner {

    private final FileInputProcessor fileInputProcessor;
    private final ManualInputProcessor manualInputProcessor;

    @Autowired
    public ConsoleController(FileInputProcessor fileInputProcessor, ManualInputProcessor manualInputProcessor) {
        this.fileInputProcessor = fileInputProcessor;
        this.manualInputProcessor = manualInputProcessor;
    }

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
