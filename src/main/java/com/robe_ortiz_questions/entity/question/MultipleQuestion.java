package com.robe_ortiz_questions.entity.question;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

@Entity
public class MultipleQuestion extends Question {

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "incorrect_answers")
	private List<String> incorrectAnswers;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "correct_answers")
	private List<String> correctAnswers;

	public MultipleQuestion() {
	}

	public MultipleQuestion(String question, CategoryOfQuestion category, List<String> incorrectAnswers,
			List<String> correctAnswer) {
		super(question, category);
		this.incorrectAnswers = incorrectAnswers;
		this.correctAnswers = correctAnswer;
	}

	public List<String> getIncorrectAnswers() {
		return incorrectAnswers;
	}

	public void setIncorrectAnswers(List<String> incorrectAnswers) {
		this.incorrectAnswers = incorrectAnswers;
	}

	public List<String> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(List<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	@JsonIgnore
	@Override
	public TypeOfQuestion getTypeOfQuestion() {
		return TypeOfQuestion.MULTIPLE_QUESTION;
	}
}
