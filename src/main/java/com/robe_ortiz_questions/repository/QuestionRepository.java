package com.robe_ortiz_questions.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
	
	Page<Question> findByActiveTrue(Pageable pageable);
	
	Page<Question> findByCategoryAndActiveTrueOrderByIdAsc(CategoryOfQuestion category, Pageable pageable);
	
	List<Question> findByCategoryAndActiveTrue(CategoryOfQuestion categoryOfQuestions);
	
	long countByCategory(CategoryOfQuestion category);
	 
	boolean existsByQuestion(String question);
}
