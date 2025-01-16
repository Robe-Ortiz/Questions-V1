package com.robe_ortiz_questions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.robe_ortiz_questions.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
			CustomOAuth2UserService customOAuth2UserService) throws Exception {
		return httpSecurity.authorizeHttpRequests(request -> {
			request.requestMatchers("/css/**", "/img/**", "/js/**").permitAll();
			request.requestMatchers("/api/**").permitAll();
			request.requestMatchers("/game/**", "/update-user-name").authenticated();
			request.requestMatchers(HttpMethod.GET, "/", "/question/all", "/question/id/*", "/question/category/**")
					.permitAll();

			request.requestMatchers("/question/new", "/question/new/question-file", "/question/edit/**",
					"/question/delete/**", "/question/load/**", "/question/upload").hasAuthority("ADMIN");
			request.anyRequest().authenticated();
		}).oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.defaultSuccessUrl("/", true))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
						.clearAuthentication(true).deleteCookies("JSESSIONID", "OAUTH2_ACCESS_TOKEN",
								"OAUTH2_AUTHORIZATION_CODE", "G_AUTHUSER_H", "G_AUTHUSER", "G_AUTH", "G_AUTHID"))
				.csrf(csrf -> csrf // Tendría que evitar esto y hacer que todo incluya el certificado csrf
						.ignoringRequestMatchers("/logout").ignoringRequestMatchers("/game/**")
						.ignoringRequestMatchers("/api/**").ignoringRequestMatchers("/question/upload")
						.ignoringRequestMatchers("/update-user-name"))
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
