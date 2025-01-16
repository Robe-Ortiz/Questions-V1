package com.robe_ortiz_questions.entity.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.user.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfQuestions;
    @Enumerated(EnumType.STRING)
    private CategoryOfQuestion categoryOfQuestions;
    @ManyToMany
    @JoinTable(
        name = "game_questions", 
        joinColumns = @JoinColumn(name = "game_id"), 
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> listOfQuestions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @CreationTimestamp
    private LocalDateTime registrationDate;  
    @ElementCollection
    @CollectionTable(
        name = "game_answers", 
        joinColumns = @JoinColumn(name = "game_id")
    )
    @Column(name = "answer")
    private List<String> answers = new ArrayList<>();
    

    public Game() {
	}

	public Game(int numberOfQuestions, CategoryOfQuestion categoryOfQuestions, List<Question> listOfQuestions, User user) {
        this.numberOfQuestions = numberOfQuestions;
        this.categoryOfQuestions = categoryOfQuestions;
        this.listOfQuestions = listOfQuestions;
        this.user = user;
    }	
	
    public void addAnswer(String answer) {
        answers.add(answer);
    }

    public Long getId() {
		return id;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void incrementCurrentQuestionIndex() {
        this.currentQuestionIndex++;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
    }

    public void incrementWrongAnswers() {
        this.wrongAnswers++;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }
    
    public User getUser() {
        return user;
    }

    public CategoryOfQuestion getCategoryOfQuestions() {
        return categoryOfQuestions;
    }

    public List<Question> getListOfQuestions() {
        return listOfQuestions;
    }
}

