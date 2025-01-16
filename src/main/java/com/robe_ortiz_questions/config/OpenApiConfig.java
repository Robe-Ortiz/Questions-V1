package com.robe_ortiz_questions.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
	    OpenAPI openAPI = new OpenAPI()
	            .info(new Info()
	                    .title("Documentación API Questions")
	                    .version("1.0")
	                    .description("<div class='api-description'>" +
	                                 "<h2>Descripción de la API</h2>" +
	                                 "<p>Esta API permite interactuar con el backend para gestionar preguntas y usuarios. "
	                                 + "Actualmente no tiene ninguna limitación y se podría consultar o borrar cualquier dato.</p>" +
	                                 "<h3>Categorías válidas:</h3>" +
	                                 "<ul>" +
	                                 String.join("", Arrays.stream(CategoryOfQuestion.values())
	                                         .map(category -> "<li>" + category.name() + "</li>")
	                                         .collect(Collectors.toList())) +
	                                 "</ul>" +
	                                 "<h3>Tipos de pregunta válidos:</h3>" +
	                                 "<ul>" +
	                                 String.join("", Arrays.stream(TypeOfQuestion.values())
	                                         .map(type -> "<li>" + type.name() + "</li>")
	                                         .collect(Collectors.toList())) +
	                                 "</ul>" +
	                                 "<div class='btn-container'>" +
	                                 "<a href='http://localhost:8080' class='btn-return'>Volver a la Aplicación</a>" +
	                                 "</div>" +
	                                 "</div>")
	            );
	    return openAPI;
	}
}
