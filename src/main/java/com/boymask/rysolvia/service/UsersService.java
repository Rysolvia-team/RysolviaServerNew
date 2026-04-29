package com.boymask.rysolvia.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boymask.rysolvia.database.users.User;
import com.boymask.rysolvia.database.users.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UsersService {
	@Autowired
	UserRepository userRepository;

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
		System.out.println(uu);
		uu.setAbbonamento(abb);
		int numToAdd = 0;
		switch (abb) {
		case "Opt1":
			numToAdd = 1;
			break;
		case "Opt2":
			numToAdd = 5;
			break;
		case "Opt3":
			numToAdd = 10;
			break;
		}
		int currVal = uu.getBolletteTotali();
		uu.setBolletteTotali(currVal + numToAdd);
		userRepository.save(uu);
	}
}
