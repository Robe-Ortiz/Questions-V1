package com.robe_ortiz_questions.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAllUser(){
		return userRepository.findAll();
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void deleteByEmail(String email) {
		userRepository.deleteById(findByEmail(email).getId());
	}

	public User save(User user) {
		return userRepository.save(user);
	}
}
