package com.boymask.rysolvia.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boymask.rysolvia.ProductsPool;
import com.boymask.rysolvia.database.prodotti.RysolviaProduct;
import com.boymask.rysolvia.database.users.User;
import com.boymask.rysolvia.database.users.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UsersService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	StatusService statusService;

	public User getUser(String id) {
		User user = userRepository.findByUserId(id);
		if (user == null) {
			user = new User();
			user.setUserid(id);
			user.setAbbonamento("FREE");
			user.setBolletteTotali(2);

		}
		user.setLastAccess((new Date()).toString());
		userRepository.save(user);
		return user;
	}

	@Transactional
	public void save(User user) {

		userRepository.save(user);

	}

	@Transactional
	public void addAbbonamento(String id, String abb) {
		User uu = userRepository.findByUserId(id);
	
		uu.setAbbonamento(abb);

		RysolviaProduct prod = ProductsPool.getProduct(abb);
		int numToAdd = prod.getNumero_bollette();
	
		int currVal = uu.getBolletteTotali();
		uu.setBolletteTotali(currVal + numToAdd);
		userRepository.save(uu);

		statusService.updateIncasso(abb);

	}
	@Transactional
	public void decBollette(String usr) {
		User user = getUser(usr);
		int currVal = user.getBolletteAnalizzate();
		user.setBolletteAnalizzate(currVal + 1);
	}
	
	@Transactional
	public boolean hasBollette( String usr ) {
		User user = getUser(usr);
		return user.getBolletteTotali()-user.getBolletteAnalizzate() >0;
	}
}
