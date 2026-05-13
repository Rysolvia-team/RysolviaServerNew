package com.boymask.rysolvia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/users")
	public String users(Model model) {
		List<User> users = usersRepo.findAll();
		model.addAttribute("users", users);
		return "users";
	}

	@GetMapping("/handle_products")
	public String handleProducts(Model model) {
		List<RysolviaProduct> products = repo.findAll();
		model.addAttribute("products", products);
		return "handle_products";
	}

	@PostMapping("/products/save")
	public String saveProduct(@ModelAttribute RysolviaProduct product) {

		repo.save(product);

		return "redirect:/web/handle_products";
	}

	@PostMapping("/products/update")
	public String updateProduct(@ModelAttribute RysolviaProduct product) {

		repo.save(product);

		return "redirect:/web/handle_products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {

		repo.deleteById(id);

		return "redirect:/web/handle_products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable Long id, Model model) {

		RysolviaProduct product = repo.findById(id).orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

		model.addAttribute("product", product);

		return "edit_product";
	}

	@GetMapping("/products/new")
	public String newProducts(Model model) {
		RysolviaProduct p = new RysolviaProduct();
		model.addAttribute("product", p);
		return "new_product";
	}

	@GetMapping("/logs")
	public String products() {

		return "logs";
	}
}