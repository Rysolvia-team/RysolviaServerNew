package com.boymask.rysolvia.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.service.GptService;
import com.boymask.rysolvia.service.UsersService;

@RestController
@RequestMapping("/api/gpt")
@CrossOrigin
public class GptController {

	@Autowired
	private GptService gptService;
	@Autowired
	private UsersService usersService;

	@PostMapping("/analyzepdf")
	public ResponseEntity<String> analyzepdf(@RequestBody Map<String, Object> payload) {

		System.out.println("PDF REQUEST");
		
		ResponseEntity<String> check = checkBollette(payload);
		if( check!=null)return check;

		ResponseEntity<String> res = gptService.analyzepdf(payload);

		return res;
	}

	@PostMapping("/analyze")
	public ResponseEntity<String> analyze(@RequestBody Map<String, Object> payload) {

		ResponseEntity<String> check = checkBollette(payload);
		if( check!=null)return check;
		
		return gptService.analyzeImages(payload);
	}

	private ResponseEntity<String> checkBollette(Map<String, Object> payload) {
		String user = (String) payload.get("user");
		if (usersService.hasBollette(user))
			return null;
		
		return ResponseEntity.status(400).body("Bollette esaurite");
	}
}