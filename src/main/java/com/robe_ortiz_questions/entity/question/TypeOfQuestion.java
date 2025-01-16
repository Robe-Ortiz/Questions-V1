package com.robe_ortiz_questions.entity.question;

public enum TypeOfQuestion {
    TRUE_OR_FALSE("Verdadero o falso"),
	MULTIPLE_QUESTION("Varias respuestas");
	
	private String translate;

	private TypeOfQuestion(String translate) {
		this.translate = translate;
	}

	public String getTranslate() {
		return translate;
	}
	
}
