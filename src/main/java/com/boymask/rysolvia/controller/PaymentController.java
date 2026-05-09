package com.boymask.rysolvia.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boymask.rysolvia.dto.PaymentRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin // per test con app Android
public class PaymentController {
	@Value("${stripe.pub.key}")
	private String stripeSecretKey;

	@PostMapping("/create-payment-intent")
	public Map<String, Object> createPaymentIntent(@RequestBody PaymentRequest request) {

		try {

			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount((long) request.getAmount())
					.setCurrency("eur")
					.setAutomaticPaymentMethods(
							PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
					.build();

			PaymentIntent intent = PaymentIntent.create(params);

			Map<String, Object> response = new HashMap<>();
			response.put("clientSecret", intent.getClientSecret());

			return response;

		} catch (Exception e) {
			throw new RuntimeException("Errore creazione pagamento", e);
		}
	}

	@GetMapping("/get")
	public Map<String,String> getPayKey() {

	    Map<String,String> map = new HashMap<>();
	    map.put("key", stripeSecretKey);

	    return map;
	}
}
