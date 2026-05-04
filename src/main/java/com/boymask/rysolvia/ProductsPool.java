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
		List<RysolviaProduct> products = repo.findAll();
		for (RysolviaProduct p : products) {
			result.put( p.getName(), p) ;
			
		}

//		ProductListParams params = ProductListParams.builder().build();
//		ProductCollection products = Product.list(params);
//
//
//		for (Product p : products.getData()) {
//
//			PriceListParams priceParams = PriceListParams.builder().setProduct(p.getId()).build();
//
//			PriceCollection prices = Price.list(priceParams);
//
//			for (Price price : prices.getData()) {
//
//				RysolviaProduct product = new RysolviaProduct();
//				product.setName(p.getName());
//				product.setDescription(p.getDescription());
//				product.setPrezzo(price.getUnitAmount());
//
//				String numBoll = p.getMetadata().get("num_bollette");
//
//				int numBollInt = Integer.parseInt(numBoll);
//
//				product.setNumero_bollette(numBollInt);
//
//				result.put( p.getName(), product) ;
//			}
//		}
	}

	
}
