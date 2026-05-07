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

@RestController
@RequestMapping("/api/gpt")
@CrossOrigin
public class GptController {

	@Autowired
	private GptService gptService;

	@PostMapping("/analyzepdf")
	public ResponseEntity<String> analyzepdf(@RequestBody Map<String, Object> payload) {

		System.out.println("PDF REQUEST");

		ResponseEntity<String> res = gptService.analyzepdf(payload);
		
		return res;
	}

	@PostMapping("/analyze")
	public ResponseEntity<String> analyze(@RequestBody Map<String, Object> payload) {

		return gptService.analyzeImages(payload);
	}
}