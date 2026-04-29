package com.boymask.rysolvia.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boymask.rysolvia.database.status.Status;
import com.boymask.rysolvia.database.users.User;
import com.boymask.rysolvia.service.StatusService;

import java.util.List;

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