package com.boymask.rysolvia.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.ProductsPool;
import com.boymask.rysolvia.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.param.PriceListParams;
import com.stripe.param.ProductListParams;

@RestController
public class RysolviaController {
	@Value("${openai.secret.key}")
	private String openaiSecretKey;

	@Autowired
	private StripeService stripeService;

	@GetMapping("/hello")
	public String hello() {
		return "Ciao Spring Boot!";
	}

	@GetMapping("/products")
	public List<Map<String, Object>> getProducts() {
		
		List<Map<String, Object>> result = new ArrayList<>();
		
		try {
			ProductListParams params = ProductListParams.builder().build();
			ProductCollection products = Product.list(params);

			for (Product p : products.getData()) {

				PriceListParams priceParams = PriceListParams.builder().setProduct(p.getId()).build();

				PriceCollection prices = Price.list(priceParams);

				for (Price price : prices.getData()) {

					if (ProductsPool.getProduct(p.getName()) == null)
						continue;

					Map<String, Object> item = new HashMap<>();
					item.put("id", p.getId());
					item.put("name", p.getName());
					item.put("description", p.getDescription());
					item.put("price", price.getUnitAmount()); // in centesimi
					item.put("currency", price.getCurrency());

					result.add(item);
				}
			}

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return result;
	}

	@GetMapping("/stripe/init")
	public ResponseEntity<Long> initStripe() {
		stripeService.init();
		return ResponseEntity.ok((long) 0);
	}
}
