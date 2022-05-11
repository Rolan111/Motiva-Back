package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class CareSheetAnswerPsychosocial extends AbstractEntity {

    private Integer idQuestion;
    private Integer idOptionAnswer;
    private String openAnswer;
    private Integer idPoll;

    @PropertyName("id_question")
    public Integer getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    @PropertyName("id_option_answer")
    public Integer getIdOptionAnswer() {
        return idOptionAnswer;
    }

    @PropertyName("id_option_answer")
    public void setIdOptionAnswer(Integer idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
    }

    @PropertyName("open_answer")
    public String getOpenAnswer() {
        return openAnswer;
    }

    @PropertyName("open_answer")
    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }

    @PropertyName("id_poll")
    public Integer getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(Integer idPoll) {
        this.idPoll = idPoll;
    }
}
