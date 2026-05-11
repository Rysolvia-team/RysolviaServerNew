package com.boymask.rysolvia.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class LogController {

    @GetMapping("/admin/logs")
    public String getLogs() throws Exception {

        return Files.readString(
                Paths.get("logs/app.log")
        );
    }
}