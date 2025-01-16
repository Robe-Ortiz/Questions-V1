package com.robe_ortiz_questions.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.entity.user.UserRole;
import com.robe_ortiz_questions.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
		
	@Autowired
    private UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	    OAuth2User oAuth2User = super.loadUser(userRequest);
	    String email = oAuth2User.getAttribute("email");
        String profilePicture = oAuth2User.getAttribute("picture");
        
	    User user = userRepository.findByEmail(email);

	    if (user == null) {
	        String name = oAuth2User.getAttribute("name");
	        user = new User(email, name,profilePicture);
	        user.setUserRole(UserRole.USER);  
	        userRepository.save(user);
	    }else {
	        String currentProfilePicture = user.getProfilePicture();
	        String newProfilePicture = oAuth2User.getAttribute("picture");
	        if (newProfilePicture != null && !newProfilePicture.equals(currentProfilePicture)) {
	            user.setProfilePicture(newProfilePicture);
	            userRepository.save(user);
	        }
	    }
	   
	    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().name());
	    

	    return new DefaultOAuth2User(Collections.singletonList(authority), oAuth2User.getAttributes(), "email");
	}

}
