package com.robe_ortiz_questions.entity.question;

import java.util.List;

public class QuestionFactory {

    @SuppressWarnings("unchecked")
    public static Question createQuestion(TypeOfQuestion typeOfQuestion, CategoryOfQuestion category, String question, Object... extraParams) throws IllegalArgumentException {
        switch (typeOfQuestion) {
            case TRUE_OR_FALSE:
                if (extraParams.length != 1 || !(extraParams[0] instanceof Boolean)) {
                    throw new IllegalArgumentException("Debe proporcionar una respuesta booleana (true / false) para una pregunta de tipo TrueOrFalse.");
                }
                return new TrueOrFalseQuestion(question, category, (Boolean) extraParams[0]);

            case MULTIPLE_QUESTION:
                if (extraParams.length != 2 || !(extraParams[0] instanceof List<?>) || !(extraParams[1] instanceof List<?>)) {
                    throw new IllegalArgumentException("Debe proporcionar una lista de respuestas incorrectas y una lista de respuestas correctas para una pregunta de tipo MultipleQuestion.");
                }
                return new MultipleQuestion(question, category, (List<String>) extraParams[0], (List<String>) extraParams[1]);
            default:
                throw new IllegalArgumentException("Tipo de pregunta no soportado.");
        }
    }
}
