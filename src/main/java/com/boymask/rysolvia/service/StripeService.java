package com.boymask.rysolvia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boymask.rysolvia.database.prodotti.RysolviaProduct;
import com.boymask.rysolvia.database.prodotti.RysolviaProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

@Service
public class StripeService {
	@Autowired
	RysolviaProductRepository repo;

	public void init() {

		List<RysolviaProduct> ll = repo.findAll();

		for (RysolviaProduct p : ll)
			createProducts(p);
	}

	private void createProducts(RysolviaProduct p) {
		try {
			ProductCreateParams productParams = ProductCreateParams.builder() //
					.setName(p.getName()) //
					.setDescription(p.getDescription()) //
					.putMetadata("num_bollette", ""+p.getNumero_bollette()) //
					.build();

			Product product = Product.create(productParams);

			System.out.println("✅ Prodotto creato!");
			System.out.println("ID: " + product.getId());

			// =========================
			// 2. CREA IL PREZZO (OBBLIGATORIO PER PAGAMENTI)
			// =========================
			PriceCreateParams priceParams = PriceCreateParams.builder() //
					.setUnitAmount(p.getPrezzo()) // 9.99 €
					.setCurrency("eur").setProduct(product.getId()).build();

			Price price = Price.create(priceParams);

			System.out.println("💰 Prezzo creato!");
			System.out.println("Price ID: " + price.getId());

		} catch (StripeException e) {
			System.err.println("❌ Errore Stripe: " + e.getMessage());
		}
	}

}
