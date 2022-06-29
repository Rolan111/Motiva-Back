package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerDto
 */
public class AnswerDto extends AbstractDto {
    private Long idAnswer;
    private Long idQuestion;
    private String openAnswer;
//    private Long idPoll;
    private String idPoll;
    private TypeQuestionaryEnum type;
    private List<Long> idOptionAnswers;
    private List<OptionAnswerDto> optionAnswerDtoList;

    public Long getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }


//    public Long getIdPoll() {
//        return idPoll;
//    }
//
//    public void setIdPoll(Long idPoll) {
//        this.idPoll = idPoll;
//    }


    public String getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }

    public List<Long> getIdOptionAnswers() {
        return idOptionAnswers;
    }

    public void setIdOptionAnswers(List<Long> idOptionAnswers) {
        this.idOptionAnswers = idOptionAnswers;
    }

    public List<OptionAnswerDto> getOptionAnswerDtoList() {
        return optionAnswerDtoList;
    }

    public void setOptionAnswerDtoList(List<OptionAnswerDto> optionAnswerDtoList) {
        this.optionAnswerDtoList = optionAnswerDtoList;
    }
}
