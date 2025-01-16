package com.robe_ortiz_questions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robe_ortiz_questions.entity.user.User;


@Controller
public class RootController {
		
	@GetMapping("/")
	public String home(Model model) {	
		return "home";
	}	
		
	@GetMapping("zero")
	public String getMethodName() {
		int i = 5/0;
		return null;
	}
	
}