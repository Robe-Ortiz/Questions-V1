package com.robe_ortiz_questions.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robe_ortiz_questions.entity.game.Game;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Partidas", description = "Operaciones relacionadas con las partidas jugadas por usuarios.")
public class GameRestController {

	@Autowired
	GameService gameService;
	
	@Operation(summary = "Obtener partida por ID", description = "Devuelve una partida por su ID único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Partida encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
			@ApiResponse(responseCode = "404", description = "Partida no encontrada", content = @Content) })
	
	@GetMapping("{id}")
	public ResponseEntity<Game> getGameById(@PathVariable Long id){
		Game game = gameService.getGameById(id);
		if(game == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(game);
	}
	
	
	@Operation(summary = "Obtener todas las partidas de un usuario", description = "Devuelve una lista de todas las partidas registradas por un usuario concreto mediante el id del usuario.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de partidas encontrada", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Game.class))),
			@ApiResponse(responseCode = "404", description = "No se encontraron partidas", content = @Content) })	
	@GetMapping("/user/{id}")
	public ResponseEntity<List<Game>> getAllGameByUserId(@PathVariable Long id){
		List<Game> games = gameService.getGamesByUserId(id);
		if(games.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(games);
	}
	
}
