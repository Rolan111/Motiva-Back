package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class QuestionEntityPrueba extends AbstractEntity {
    private int idQuestion;
    private String description;

    @PropertyName("id_question")
    public int getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
