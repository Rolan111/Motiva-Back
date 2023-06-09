package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.QuestionTypeEnum;
import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuestionDto
 */
public class QuestionDto extends AbstractDto {
    private Long idQuestion;
    private String description;
    private Long idFather;
    private Long order;
    private TypeQuestionaryEnum type;
    private QuestionTypeEnum questionaryType;
    private List<OptionAnswerDto> optionAnswerDtoList;

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

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }

    public QuestionTypeEnum getQuestionaryType() {
        return questionaryType;
    }

    public void setQuestionaryType(QuestionTypeEnum questionaryType) {
        this.questionaryType = questionaryType;
    }
}
