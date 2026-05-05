package com.boymask.rysolvia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.database.users.User;
import com.boymask.rysolvia.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	@Autowired
	UsersService usersService;

	@GetMapping("/get/{id}")
	public User getUser(@PathVariable  String id) {
		User user = usersService.getUser(id);

		return user;
	}
	
	 // PUT (update)
    @PutMapping("/put/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @RequestBody User user) {

    	usersService.save(user);
    	return ResponseEntity.ok(user);
    }
    
    @PutMapping("/abbonamento/put/{id}/{abb}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @PathVariable String abb,
            @RequestBody User user) {
		System.out.println("addAbbonamento "+id+ " "+abb);
		usersService.addAbbonamento(id, abb);
    	return ResponseEntity.ok(user);
    }
}
