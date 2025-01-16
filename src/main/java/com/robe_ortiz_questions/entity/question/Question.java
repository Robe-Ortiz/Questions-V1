package com.robe_ortiz_questions.entity.question;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeOfQuestion")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TrueOrFalseQuestion.class, name = "TRUE_OR_FALSE"),
    @JsonSubTypes.Type(value = MultipleQuestion.class, name = "MULTIPLE_QUESTION"),
})
public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "question", unique = true)
    private String question;

    @Enumerated(EnumType.STRING)  
    @Column(name = "category")
    private CategoryOfQuestion category;
    
    @Column(name = "active")
    private boolean active = true;
    
    @Column(name = "last_modification")
    private LocalDateTime lastModification = LocalDateTime.now();
    
	public Question() {
	}


	public Question(String question, CategoryOfQuestion category) {
	    this.question = question;
	    this.category = category;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	} 		
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}


	public CategoryOfQuestion getCategory() {
		return category;
	}

	public void setCategory(CategoryOfQuestion category) {
		this.category = category;
	}

	public abstract TypeOfQuestion getTypeOfQuestion();
}
