package com.robe_ortiz_questions.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.QuestionFactory;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;
import com.robe_ortiz_questions.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/question")
@Tag(name = "Preguntas", description = "Operaciones relacionadas con las preguntas.")
public class QuestionRestController {

	@Autowired
	private QuestionService questionService;
	
	private Question createQuestionFromData(Question newQuestionData) {
	    if (TypeOfQuestion.MULTIPLE_QUESTION.equals(newQuestionData.getTypeOfQuestion())) {
	        return QuestionFactory.createQuestion(
	                TypeOfQuestion.MULTIPLE_QUESTION,  
	                newQuestionData.getCategory(),
	                newQuestionData.getQuestion(),
	                ((MultipleQuestion) newQuestionData).getIncorrectAnswers(),
	                ((MultipleQuestion) newQuestionData).getCorrectAnswers());
	    } else if (TypeOfQuestion.TRUE_OR_FALSE.equals(newQuestionData.getTypeOfQuestion())) {
	        return QuestionFactory.createQuestion(
	                TypeOfQuestion.TRUE_OR_FALSE,  
	                newQuestionData.getCategory(),
	                newQuestionData.getQuestion(),
	                ((TrueOrFalseQuestion) newQuestionData).getAnswer());
	    } else {
	        throw new IllegalArgumentException("Tipo de pregunta no soportado");
	    }
	}

	@Operation(summary = "Crear una nueva pregunta", 
			description = "Crea una nueva pregunta en la base de datos utilizando los datos proporcionados en el cuerpo de la solicitud. "
			+ "El id no es necesario enviarlo, se gestionará de forma automática.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Pregunta creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
			@ApiResponse(responseCode = "400", description = "Datos de la solicitud incorrectos", content = @Content(mediaType = "application/json")) })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la nueva pregunta", required = true, content = @Content(mediaType = "application/json", examples = {
			@ExampleObject(name = "MultipleChoiceQuestion", summary = "Ejemplo de pregunta de opción múltiple", value = """
					{
					  "typeOfQuestion": "MULTIPLE_CHOICE",
					  "category": "PROGRAMACIÓN",
					  "question": "¿Cuál de estas opciones es correcta?",
					  "correctAnswers": ["Respuesta correcta1","Respuesta correcta2","Respuesta correcta3","Respuesta correcta4","Respuesta correctaX"],
					  "incorrectAnswers": ["Respuesta incorrecta 1", "Respuesta incorrecta 2","Respuesta incorrecta 3","Respuesta incorrecta 4","Respuesta incorrecta X"]
					}
					"""),
			@ExampleObject(name = "TrueOrFalseQuestion", summary = "Ejemplo de pregunta de verdadero o falso", value = """
					{
					  "typeOfQuestion": "TRUE_OR_FALSE",
					  "category": "PROGRAMACIÓN",
					  "question": "¿Esto funciona?",
					  "answer": true
					}
					""") }))
	@PostMapping("/create")
	public ResponseEntity<Question> createQuestion(@RequestBody Question newQuestionData) {
	    try {
	        Question newQuestion = createQuestionFromData(newQuestionData);	        
	        Question savedQuestion = questionService.save(newQuestion);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);	        
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}
	
	@Operation(summary = "Obtener todas las preguntas", description = "Devuelve una lista de todas las preguntas registradas en el sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de preguntas encontrada", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Question.class))),
			@ApiResponse(responseCode = "404", description = "No se encontraron preguntas", content = @Content) })
	@GetMapping("/all")
	public ResponseEntity<List<Question>> getAllQuestions() {
		List<Question> questions = questionService.findAllQuestions();
		if (questions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(questions);
	}

	@Operation(summary = "Obtener pregunta por ID", description = "Devuelve una pregunta por su ID único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pregunta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
			@ApiResponse(responseCode = "404", description = "Pregunta no encontrada", content = @Content) })
	@GetMapping("{id}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
		Question question = questionService.getQuestionById(id);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(question);
	}

	@Operation(summary = "Actualizar pregunta por ID", description = "Actualiza una pregunta existente según su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pregunta actualizada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
			@ApiResponse(responseCode = "404", description = "Pregunta no encontrada", content = @Content) })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la pregunta a actualizar (incluyendo el ID)", required = true, content = @Content(mediaType = "application/json", examples = {
		    @ExampleObject(name = "MultipleChoiceQuestionUpdated", summary = "Ejemplo de actualización de pregunta de opción múltiple", value = """
		            {
		              "id": 1,
		              "typeOfQuestion": "MULTIPLE_QUESTION",
		              "category": "PROGRAMACIÓN",
		              "question": "¿Cuál de estas opciones es correcta?",
		              "correctAnswers": ["Respuesta correcta1","Respuesta correcta2","Respuesta correcta3","Respuesta correcta4","Respuesta correctaX"],
		              "incorrectAnswers": ["Respuesta incorrecta 1", "Respuesta incorrecta 2","Respuesta incorrecta 3","Respuesta incorrecta 4","Respuesta incorrecta X"]
		            }
		            """),
		    @ExampleObject(name = "TrueOrFalseQuestionUpdated", summary = "Ejemplo de actualización de pregunta de verdadero o falso", value = """
		            {
		              "id": 2,
		              "typeOfQuestion": "TRUE_OR_FALSE",
		              "category": "PROGRAMACIÓN",
		              "question": "¿Esto funciona ahora?",
		              "answer": false
		            }
		            """)
		}))
	@PutMapping("{id}")
	public ResponseEntity<Question> updateQuestionById(@PathVariable Long id, @RequestBody Question updatedQuestion) {
		Question question = questionService.getQuestionById(id);

		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		if (question instanceof MultipleQuestion && updatedQuestion instanceof MultipleQuestion) {
			MultipleQuestion multipleQuestion = (MultipleQuestion) question;
			MultipleQuestion updatedMultipleQuestion = (MultipleQuestion) updatedQuestion;
			multipleQuestion.setQuestion(updatedMultipleQuestion.getQuestion());
			multipleQuestion.setCorrectAnswers(updatedMultipleQuestion.getCorrectAnswers());
			multipleQuestion.setIncorrectAnswers(updatedMultipleQuestion.getIncorrectAnswers());
		} else if (question instanceof TrueOrFalseQuestion && updatedQuestion instanceof TrueOrFalseQuestion) {
			TrueOrFalseQuestion trueOrFalseQuestion = (TrueOrFalseQuestion) question;
			TrueOrFalseQuestion updatedTrueOrFalseQuestion = (TrueOrFalseQuestion) updatedQuestion;
			trueOrFalseQuestion.setQuestion(updatedTrueOrFalseQuestion.getQuestion());
			trueOrFalseQuestion.setAnswer(updatedTrueOrFalseQuestion.getAnswer());
		}

		Question savedQuestion = questionService.save(question);
		return ResponseEntity.ok(savedQuestion);
	}
}