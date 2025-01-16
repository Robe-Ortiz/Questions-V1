package com.robe_ortiz_questions.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	public void updateQuestion(Long id, Question updatedQuestion) {
		Question existingQuestion = questionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada con ID: " + id));

		existingQuestion.setQuestion(updatedQuestion.getQuestion());
		existingQuestion.setCategory(updatedQuestion.getCategory());

		if (existingQuestion instanceof TrueOrFalseQuestion && updatedQuestion instanceof TrueOrFalseQuestion) {
			((TrueOrFalseQuestion) existingQuestion).setAnswer(((TrueOrFalseQuestion) updatedQuestion).getAnswer());
		} else if (existingQuestion instanceof MultipleQuestion && updatedQuestion instanceof MultipleQuestion) {
			((MultipleQuestion) existingQuestion)
					.setCorrectAnswers(((MultipleQuestion) updatedQuestion).getCorrectAnswers());
			((MultipleQuestion) existingQuestion)
					.setIncorrectAnswers(((MultipleQuestion) updatedQuestion).getIncorrectAnswers());
		}

		questionRepository.save(existingQuestion);
	}

	public void deleteAll() {
		questionRepository.deleteAll();
	}

	public List<Question> findAllQuestions() {
		return questionRepository.findAll();
	}

	public Question getQuestionById(long id) {
		return questionRepository.findById(id).orElse(null);
	}

	public void desactivateById(Long id) {
		Question question = questionRepository.findById(id)
		        .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada " + id));
		question.setActive(false);
		questionRepository.save(question);
	}

	public Question save(Question question) {
		return questionRepository.save(question);
	}

	public Page<Question> getAllQuestionsActivesPageables() {
		return getAllQuestionsActivesPageables(0,10);
	}

	public Page<Question> getAllQuestionsActivesPageables(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
		return questionRepository.findByActiveTrue(pageable);
	}

	public Page<Question> getAllQuestionsActivesPageables(CategoryOfQuestion category, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return questionRepository.findByCategoryAndActiveTrueOrderByIdAsc(category, pageable);
	}

	public List<Integer> getNumbersOfPages(Page<Question> questionPage) {
		return IntStream.range(0, questionPage.getTotalPages()).boxed().collect(Collectors.toList());
	}

	public void saveQuestion(Question question) {
		questionRepository.save(question);
	}

	private void saveQuestionsFromFile(List<Question> questions) {
		for (Question question : questions) {
			if (!questionRepository.existsByQuestion(question.getQuestion())) {
				if (question instanceof MultipleQuestion
						&& ((MultipleQuestion) question).getIncorrectAnswers().size() < 3) {
					System.err.println("Error tamaño incorrect answer");
					throw new IllegalArgumentException(
							"Todas las preguntas de múltiple elección deben tener como mínimo tres respuestas incorrectas.");
				} else {
					questionRepository.save(question);
				}
			}
		}
	}

	public void processQuestionsFromTheServerFile(String classPath) throws IOException, IllegalArgumentException {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource(classPath);
		String jsonString = new String(Files.readAllBytes(resource.getFile().toPath()));
		List<Question> questions = objectMapper.readValue(jsonString,
				objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
		saveQuestionsFromFile(questions);
	}

	public void processQuestionsFromTheFormFile(String jsonContent) throws IOException, IllegalArgumentException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Question> questions = objectMapper.readValue(jsonContent,
				objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
		saveQuestionsFromFile(questions);
	}
}