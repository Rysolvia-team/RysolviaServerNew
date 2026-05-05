package com.boymask.rysolvia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boymask.rysolvia.database.prodotti.RysolviaProduct;
import com.boymask.rysolvia.database.prodotti.RysolviaProductRepository;
import com.boymask.rysolvia.database.users.User;
import com.boymask.rysolvia.database.users.UserRepository;
import com.boymask.rysolvia.service.StatusService;

@Controller
@RequestMapping("/web")
public class WebController {
	@Autowired
	StatusService statusService;
	@Autowired
	RysolviaProductRepository repo;
	@Autowired
	UserRepository usersRepo;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("status", statusService.getStatus());
		
		List<User> users = usersRepo.findAll();
		model.addAttribute("nusers", users.size());

		return "home";
	}

	@GetMapping("/products")
	public String products(Model model) {
		List<RysolviaProduct> products = repo.findAll();
		model.addAttribute("products", products);
		return "products";
	}
}