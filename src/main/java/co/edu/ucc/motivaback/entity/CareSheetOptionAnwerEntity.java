package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class CareSheetOptionAnwerEntity extends AbstractEntity {

    private int idOptionAnswer;
    private int idQuestion;
    private String description;

    @PropertyName("id_option_answer")
    public int getIdOptionAnswer() {
        return idOptionAnswer;
    }

    @PropertyName("id_option_answer")
    public void setIdOptionAnswer(int idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
    }

    @PropertyName("id_question")
    public int getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }
}
