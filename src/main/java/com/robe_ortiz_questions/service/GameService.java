package com.robe_ortiz_questions.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.robe_ortiz_questions.entity.game.Game;
import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.repository.GameRepository;
import com.robe_ortiz_questions.repository.QuestionRepository;

@Service
public class GameService {

	@Autowired
	private QuestionRepository questionRepository;
	
    @Autowired
    private GameRepository gameRepository;
		
	public List<Question> getQuestions(int numberOfQuestions, CategoryOfQuestion categoryOfQuestions){
		List<Question> listOfQuestions = questionRepository.findByCategoryAndActiveTrue(categoryOfQuestions);
		Collections.shuffle(listOfQuestions);
		return listOfQuestions.stream().limit(numberOfQuestions).toList();
	}
	
    public long getTotalGamesByUserId(Long userId) {
        return gameRepository.countByUserId(userId);
    }
	
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
    
    public Game getGameById(long id) {
    	Game game = gameRepository.findById(id).orElse(null);
    	if(game == null) return new Game();
    	return game;
    }
    
    public List<Game> getGamesByUserId(Long userId) {
        return gameRepository.findAll().stream()
                .filter(game -> game.getUser().getId().equals(userId))
                .toList();
    }
    public Page<Game> getGamesByUserIdPageables(Long userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return gameRepository.findAllByUserId(userId, pageable);
    }
    
    public List<Question> getQuestionByGameId(Long gameId){
    	Game game = gameRepository.findById(gameId).orElse(null);
    	if(game != null) return game.getListOfQuestions();
    	return null;
    }
    

	public Page<Game> getAllGamesPageables(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
		return gameRepository.findAll(pageable);
	}

	public List<Integer> getNumbersOfPages(Page<Game> questionPage) {
		return IntStream.range(0, questionPage.getTotalPages()).boxed().collect(Collectors.toList());
	}
}
