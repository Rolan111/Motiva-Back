package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

public class QuestionTypeDto {
    @SerializedName("id_question_type")
    private Long idQuestionType;
    private String description;

    public Long getIdQuestionType() {
        return idQuestionType;
    }

    public void setIdQuestionType(Long idQuestionType) {
        this.idQuestionType = idQuestionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
