package com.boymask.rysolvia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boymask.rysolvia.database.prodotti.RysolviaProduct;
import com.boymask.rysolvia.database.prodotti.RysolviaProductRepository;

import jakarta.annotation.PostConstruct;

@Component
public class ProductsPool {
	@Autowired
	RysolviaProductRepository repo;
	private static Map<String, RysolviaProduct> result = new HashMap<>();
	

	public static RysolviaProduct getProduct(String abb) {
		
		return result.get(abb);
	}
	
	@PostConstruct
	private void getProducts() throws Exception {
		initProductsDatabase();
		
		List<RysolviaProduct> products = repo.findAll();
		for (RysolviaProduct p : products) {
			result.put( p.getName(), p) ;
			
		}
	}

	private void initProductsDatabase() {
		List<RysolviaProduct> products = repo.findAll();
		if( !products.isEmpty())return;
		
		RysolviaProduct p1 = new RysolviaProduct();
		p1.setName("Prd1");
		p1.setNumero_bollette(5);
		p1.setPrezzo(500);
		repo.save(p1);
		
		RysolviaProduct p2 = new RysolviaProduct();
		p2.setName("Prd2");
		p2.setNumero_bollette(10);
		p2.setPrezzo(1000);
		repo.save(p2);
		
		RysolviaProduct p3 = new RysolviaProduct();
		p3.setName("Prd1");
		p3.setNumero_bollette(15);
		p3.setPrezzo(1500);
		repo.save(p3);
	}
	
}
