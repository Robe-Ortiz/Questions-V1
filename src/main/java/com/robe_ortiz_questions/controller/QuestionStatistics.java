package com.robe_ortiz_questions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.repository.QuestionRepository;

@Controller
public class QuestionStatistics {

	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/question-statistics")
	public String showGraphic(Model model) {
		
		CategoryOfQuestion[] categories = CategoryOfQuestion.values();
		long [] numberOfQuestionsPerCategory = new long[categories.length];
		
		for(int i = 0 ; i < categories.length; i++) {
			numberOfQuestionsPerCategory[i] = questionRepository.countByCategory(categories[i]);
		}
		model.addAttribute("categories", categories);
		model.addAttribute("numberOfQuestionsPerCategory", numberOfQuestionsPerCategory);
		
		return "question-statistics";
	}
}
