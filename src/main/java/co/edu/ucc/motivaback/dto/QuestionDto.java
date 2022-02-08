package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuestionDto
 */
public class QuestionDto {
    @SerializedName("id_question")
    private Long idQuestion;
    private String description;
    @SerializedName("id_father")
    private Long idFather;
    @SerializedName("id_question_type")
    private Long idQuestionType;
    private Long order;
    private String type;
    private List<OptionAnswerDto> optionAnswerDtoList;
    private QuestionTypeDto questionTypeDto;

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

    public Long getIdFather() {
        return idFather;
    }

    public void setIdFather(Long idFather) {
        this.idFather = idFather;
    }

    public Long getIdQuestionType() {
        return idQuestionType;
    }

    public void setIdQuestionType(Long idQuestionType) {
        this.idQuestionType = idQuestionType;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public List<OptionAnswerDto> getOptionAnswerDtoList() {
        return optionAnswerDtoList;
    }

    public void setOptionAnswerDtoList(List<OptionAnswerDto> optionAnswerDtoList) {
        this.optionAnswerDtoList = optionAnswerDtoList;
    }

    public QuestionTypeDto getQuestionTypeDto() {
        return questionTypeDto;
    }

    public void setQuestionTypeDto(QuestionTypeDto questionTypeDto) {
        this.questionTypeDto = questionTypeDto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
