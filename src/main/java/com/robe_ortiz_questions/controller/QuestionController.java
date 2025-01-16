package com.robe_ortiz_questions.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.QuestionFactory;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;
import com.robe_ortiz_questions.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/all")
	public String showAllQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
								   @RequestParam(required = false) CategoryOfQuestion category,Model model) {
	    Page<Question> questionPage;
	    if (category != null) {
	        questionPage = questionService.getAllQuestionsActivesPageables(category, page, size);
	        model.addAttribute("category", category.toString());
	    } else {
	        questionPage = questionService.getAllQuestionsActivesPageables(page, size);
	        model.addAttribute("category", "");
	    }
		model.addAttribute("questions", questionPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", questionPage.getTotalPages());
		model.addAttribute("pageNumbers", questionService.getNumbersOfPages(questionPage));		
		return "questions";
	}

	@GetMapping("/new")
	public String addQuestion(Model model, @RequestParam(required = false) String stage,
			@RequestParam(required = false) String questionType, @RequestParam(required = false) String category) {

		String currentStage = (stage == null) ? "first" : stage;
		model.addAttribute("stage", currentStage);
		model.addAttribute("categories", CategoryOfQuestion.values());
		
		if ("second".equals(currentStage)) {
			model.addAttribute("questionType", questionType);
			model.addAttribute("category", category);
		}
		return "question-add";
	}

	@PostMapping("/new")
	public String processFormToAddNewQuestion(@RequestParam String questionType, @RequestParam String category,
			RedirectAttributes redirectAttributes) {
		if (!"MULTIPLE_QUESTION".equals(questionType) && !"TRUE_OR_FALSE".equals(questionType)) {
			return "redirect:/question/new";
		}
		redirectAttributes.addAttribute("stage", "second");
		redirectAttributes.addAttribute("questionType", questionType);
		redirectAttributes.addAttribute("category", category);
		return "redirect:/question/new";
	}
	
	private void validateFields(String question, String correctAnswers, String incorrectAnswers)throws IllegalArgumentException {
		if(question.trim().isEmpty())	throw new IllegalArgumentException("El campo pregunta no puede ser un espacio en blanco.");
	    
		if(correctAnswers != null) {
			 List<String> correctAnswersList = new ArrayList<>(Arrays.asList(correctAnswers.split(",")));
		    for (String answer : correctAnswersList) {
		        if (answer.trim().isEmpty()) {
		            throw new IllegalArgumentException("Las respuestas correctas no pueden contener espacios en blanco.");
		        }
		    }
		}
		
		if(incorrectAnswers != null) {
	        List<String> incorrectAnswersList = new ArrayList<>(Arrays.asList(incorrectAnswers.split(",")));	
	        if (incorrectAnswersList.size() < 3)  throw new IllegalArgumentException("Debe haber al menos 3 respuestas incorrectas"); 
	        for (String answer : incorrectAnswersList) {
	            if (answer.trim().isEmpty()) {
	                throw new IllegalArgumentException("Las respuestas incorrectas no pueden contener espacios en blanco.");
	            }
	        }
		}
	}

	@PostMapping("/save")
	public String saveOrUpdateQuestion(@RequestParam(required = false) Long id, 
	                                   @RequestParam String question, 
	                                   @RequestParam(required = false) String correctAnswers,
	                                   @RequestParam(required = false) String incorrectAnswers, 
	                                   @RequestParam(required = false) String answer,
	                                   @RequestParam String questionType, 
	                                   @RequestParam String category, 
	                                   RedirectAttributes redirectAttributes) {
	    try {
	    	validateFields(question,correctAnswers,incorrectAnswers);
	        TypeOfQuestion typeOfQuestion = TypeOfQuestion.valueOf(questionType);
	        CategoryOfQuestion categoryOfQuestion = CategoryOfQuestion.valueOf(category);

	        Object[] extraParams;
	        if (typeOfQuestion == TypeOfQuestion.MULTIPLE_QUESTION) {	          	                                  
	            extraParams = new Object[] {
	                    new ArrayList<>(Arrays.asList(incorrectAnswers.split(","))),
	                    new ArrayList<>(Arrays.asList(correctAnswers.split(",")))
	                };
	        } else if (typeOfQuestion == TypeOfQuestion.TRUE_OR_FALSE) {
	            extraParams = new Object[] { Boolean.parseBoolean(answer) };
	        } else {
	            throw new IllegalArgumentException("Tipo de pregunta no válido");
	        }

	        Question questionEntity;

	        if (id != null) {
	            questionEntity = questionService.getQuestionById(id);
	            if (questionEntity == null) {
	                throw new IllegalArgumentException("Pregunta no encontrada con ID: " + id);
	            }	            
	            questionEntity.setQuestion(question);
	            questionEntity.setCategory(categoryOfQuestion);
	            questionEntity.setLastModification(LocalDateTime.now());
	            

	            if (typeOfQuestion == TypeOfQuestion.MULTIPLE_QUESTION && questionEntity instanceof MultipleQuestion) {
	                ((MultipleQuestion) questionEntity).setCorrectAnswers((List<String>) extraParams[1]);
	                ((MultipleQuestion) questionEntity).setIncorrectAnswers((List<String>) extraParams[0]);
	            } else if (typeOfQuestion == TypeOfQuestion.TRUE_OR_FALSE && questionEntity instanceof TrueOrFalseQuestion) {
	                ((TrueOrFalseQuestion) questionEntity).setAnswer((Boolean) extraParams[0]);
	            }
	        } else {	            
	            questionEntity = QuestionFactory.createQuestion(typeOfQuestion, categoryOfQuestion, question, extraParams);
	        }
	        
	        questionService.save(questionEntity);
	        
	        redirectAttributes.addFlashAttribute("success", "Pregunta " + (id != null ? "actualizada" : "guardada") + " con éxito");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error al guardar la pregunta. " + e.getMessage());
	        e.printStackTrace();
	    }
	    return "redirect:/question/all";
	}


	
	@GetMapping("/edit/{id}")
	public String editQuestion(@PathVariable Long id, Model model) {
	    Question question = questionService.getQuestionById(id);
	    if (question == null) {
	        throw new IllegalArgumentException("Pregunta no encontrada con ID: " + id);
	    }

	    model.addAttribute("question", question);
	    model.addAttribute("questionText", question.getQuestion());
	    model.addAttribute("categories", CategoryOfQuestion.values());

	    if (question instanceof TrueOrFalseQuestion) {
	        model.addAttribute("isTrueOrFalse", true);
	        model.addAttribute("questionType", TypeOfQuestion.TRUE_OR_FALSE);
	        model.addAttribute("answer", ((TrueOrFalseQuestion) question).getAnswer());
	    } else if (question instanceof MultipleQuestion) {
	        model.addAttribute("isMultipleChoice", true);
	        model.addAttribute("questionType", TypeOfQuestion.MULTIPLE_QUESTION);
	        model.addAttribute("correctAnswers", ((MultipleQuestion) question).getCorrectAnswers());
	        model.addAttribute("incorrectAnswers", ((MultipleQuestion) question).getIncorrectAnswers());
	    }

	    return "question-edit";
	} 


	@GetMapping("/delete/all")
	public String deleteAll(RedirectAttributes redirectAttributes) {
		questionService.deleteAll();
		redirectAttributes.addFlashAttribute("success", "Todas las preguntas se han borrado.");
		return "redirect:/question/all";
	}
	
	@PostMapping("/desactive/{id}")
	public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    questionService.desactivateById(id);
	    redirectAttributes.addFlashAttribute("success", "Pregunta desactivada.");
	    return "redirect:/question/all";
	}

	@GetMapping("/id/{id}")
	public String showQuestionById(@PathVariable long id, Model model) {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("question", question);
		model.addAttribute("activateBackButton", true);
		return "question-info";
	}
	
	

	@GetMapping("/load/{fileName}")
	public String processQuestionsFromTheServerFile(@PathVariable("fileName") String fileName, Model model, RedirectAttributes redirectAttributes) {
		try {
			questionService.processQuestionsFromTheServerFile("static/data/" + fileName + ".json");
			model.addAttribute("questions", questionService.getAllQuestionsActivesPageables());
			redirectAttributes.addFlashAttribute("success", "Todas las preguntas del archivo han sido incluidas correctamente.");
		} catch (IOException | IllegalArgumentException e) {			
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/question/all";
	}

	@GetMapping("/new/question-file")
	public String addQuestionFile(Model model) {
		return "add-question-file";
	}

	@PostMapping("/upload")
	public String uploadQuestionFromTheFormFile(@RequestParam MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		try {
			String jsonContent = new String(file.getBytes());
			
			 ObjectMapper objectMapper = new ObjectMapper();
		        objectMapper.readTree(jsonContent);
		        
			questionService.processQuestionsFromTheFormFile(jsonContent);
			
			model.addAttribute("questions", questionService.getAllQuestionsActivesPageables());
			redirectAttributes.addFlashAttribute("success", "Todo bien");
			
			return "redirect:/question/all";
		} catch (IOException | IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "add-question-file";
		}
	}
}
