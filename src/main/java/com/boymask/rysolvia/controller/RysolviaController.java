package com.boymask.rysolvia.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.controller.beans.UpdaterTokenBean;
import com.boymask.rysolvia.database.status.StatusRepository;
import com.boymask.rysolvia.service.StatusService;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.param.PriceListParams;
import com.stripe.param.ProductListParams;

@RestController
public class RysolviaController {
	@Autowired
	private StatusRepository statusRepository;
	@Autowired
	private StatusService statusService;

	@GetMapping("/hello")
	public String hello() {
		return "Ciao Spring Boot!";
	}

	@GetMapping("/products")
	public List<Map<String, Object>> getProducts() throws Exception {

		ProductListParams params = ProductListParams.builder().build();
		ProductCollection products = Product.list(params);

		List<Map<String, Object>> result = new ArrayList<>();

		for (Product p : products.getData()) {

			PriceListParams priceParams = PriceListParams.builder().setProduct(p.getId()).build();

			PriceCollection prices = Price.list(priceParams);

			for (Price price : prices.getData()) {

				Map<String, Object> item = new HashMap<>();
				item.put("id", p.getId());
				item.put("name", p.getName());
				item.put("description", p.getDescription());
				item.put("price", price.getUnitAmount()); // in centesimi
				item.put("currency", price.getCurrency());

				result.add(item);
			}
		}
	
		return result;
	}

//	@Transactional
//	private void testInsert() {
//		Status s = new Status();
//		s.setToken_totali(100);
//		s.setBollette_totali(50);
//		s.setIncassoTotale(new BigDecimal("1234.56"));
//
//		statusRepository.save(s);
//	}
	
    @PutMapping("/update_token")
    public ResponseEntity<Long>  updateToken(@RequestBody UpdaterTokenBean bean) {
    	System.out.println("Update token "+bean.getToken());
    	System.out.println("ic "+bean.isIncBollette());
    	statusService.updateTokens(bean.getToken(),bean.isIncBollette());
    	return ResponseEntity.ok((long)0);
    }
}
