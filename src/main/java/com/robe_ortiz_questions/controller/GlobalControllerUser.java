package com.robe_ortiz_questions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.service.UserService;

@ControllerAdvice
public class GlobalControllerUser {

	@Autowired
	private UserService userService;
	
	@ModelAttribute("user")
	public User sesionUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            String email = oAuth2User.getAttribute("email");
            return userService.findByEmail(email);
        }
        return null;
	}
}
