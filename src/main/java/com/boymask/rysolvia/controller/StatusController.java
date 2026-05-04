package com.boymask.rysolvia.controller;



import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.database.status.Status;
import com.boymask.rysolvia.service.StatusService;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final StatusService service;

    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping
    public List<Status> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Status create(@RequestBody Status s) {
        return service.save(s);
    }
   

}