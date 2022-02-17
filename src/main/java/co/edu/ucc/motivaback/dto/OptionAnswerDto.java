package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;
import com.google.gson.annotations.SerializedName;

public class OptionAnswerDto {
    @SerializedName("id_option_answer")
    private Long idOptionAnswer;
    @SerializedName("id_question")
    private Long idQuestion;
    private String description;
    private Long order;
    private TypeQuestionaryEnum type;

    public Long getIdOptionAnswer() {
        return idOptionAnswer;
    }

    public void setIdOptionAnswer(Long idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }
}
