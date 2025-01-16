package com.robe_ortiz_questions.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "true_or_false_question")
public class TrueOrFalseQuestion extends Question{

	@Column(name = "answer")
    private Boolean answer;

	public TrueOrFalseQuestion() {}
	
	public TrueOrFalseQuestion(String question, CategoryOfQuestion category, Boolean answer) {
		super(question, category);
		this.answer = answer;
	}

	public Boolean getAnswer() {
		return answer;
	}

	public void setAnswer(Boolean answer) {
		this.answer = answer;
	}

	@JsonIgnore
	@Override
	public TypeOfQuestion getTypeOfQuestion() {
		return TypeOfQuestion.TRUE_OR_FALSE;
	}   	
}
