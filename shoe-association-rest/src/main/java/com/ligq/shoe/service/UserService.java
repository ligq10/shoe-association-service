package com.ligq.shoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligq.shoe.entity.User;
import com.ligq.shoe.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public  User findOneUserById(String uuid){
		User user = userRepository.findOne(uuid);
		return user;
	}
}
