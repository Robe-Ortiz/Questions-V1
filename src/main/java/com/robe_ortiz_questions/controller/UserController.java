package com.robe_ortiz_questions.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robe_ortiz_questions.entity.game.Game;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.entity.user.UserRole;
import com.robe_ortiz_questions.service.CustomOAuth2UserService;
import com.robe_ortiz_questions.service.GameService;
import com.robe_ortiz_questions.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/profile")
	public String profile(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,Model model, @ModelAttribute("user") User user) {
		
		Page<Game> gamePage = gameService.getGamesByUserIdPageables(user.getId(), page,size);
		model.addAttribute("games", gamePage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", gamePage.getTotalPages());
		model.addAttribute("pageNumbers", gameService.getNumbersOfPages(gamePage));	
				
		model.addAttribute("totalGames", gameService.getTotalGamesByUserId(user.getId()));
		return "profile";
	}

	@GetMapping("/show-game/{id}")
	public String showGame(@PathVariable long id, Model model) {
		Game game = gameService.getGameById(id);
		List<Question> questions = game.getListOfQuestions();
		
	    List<Question> modifiedQuestions = questions.stream()
	            .filter(question -> question.getLastModification().isAfter(game.getRegistrationDate()))
	            .toList();
	    
		model.addAttribute("questions", game.getListOfQuestions());
		model.addAttribute("modifiedQuestions", modifiedQuestions);
		model.addAttribute("answers", game.getAnswers());
		return "game-info";
	}

	@PostMapping("/update-user-name")
	public String updateUserName(@RequestParam String name, @ModelAttribute("user") User user) {
		if (name != null && !name.isEmpty()) {
			user.setName(name);
			userService.save(user);
		}
		return "redirect:/profile";
	}

	@PostMapping("/promote-user")
	public String promoteTheUserRole(@RequestParam("password") String password,
			@ModelAttribute("user") User authenticatedUser, RedirectAttributes redirectAttributes) {

		final String hashedPassword = "$2a$10$2mvuP8EpbwwXjLpLHPrIOe52eNsHk59JSB0xvpNngVRubVswx/3pW";

		if (!passwordEncoder.matches(password, hashedPassword)) {
			redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta.");
			return "redirect:/";
		}

		User user = userService.findByEmail(authenticatedUser.getEmail());
		if (user != null && user.getUserRole() == UserRole.USER) {
			user.setUserRole(UserRole.ADMIN);
			userService.save(user);

			// refresh browser session
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
			updatedAuthorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));
			Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),
					updatedAuthorities);
			SecurityContextHolder.getContext().setAuthentication(newAuth);

			redirectAttributes.addFlashAttribute("success", "Ahora dispones de un rol de administrador.");
		}
		return "redirect:/";
	}
}
