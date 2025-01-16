package com.robe_ortiz_questions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.robe_ortiz_questions.entity.game.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	long countByUserId(Long userId);
	Page<Game> findAllByUserId(Long userId, Pageable pageable);

}