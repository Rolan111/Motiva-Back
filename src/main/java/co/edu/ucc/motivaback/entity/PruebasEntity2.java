package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;
import com.google.cloud.firestore.annotation.PropertyName;

import java.util.List;

public class PruebasEntity2 { //En este caso para answer

    private Long idAnswer;
    private Long idQuestion;
    private List<Long> idOptionAnswers;
    private String openAnswer;
    //    private Long idPoll;
    private String idPoll;
    private TypeQuestionaryEnum type;
    private String createdAt;

    @PropertyName("id_answer")
    public Long getIdAnswer() {
        return idAnswer;
    }

    @PropertyName("id_answer")
    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    @PropertyName("id_question")
    public Long getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
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
    public String getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    @PropertyName("type")
    public TypeQuestionaryEnum getType() {
        return type;
    }

    @PropertyName("type")
    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }

    @PropertyName("id_option_answers")
    public List<Long> getIdOptionAnswers() {
        return idOptionAnswers;
    }

    @PropertyName("id_option_answers")
    public void setIdOptionAnswers(List<Long> idOptionAnswers) {
        this.idOptionAnswers = idOptionAnswers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PruebasEntity2(String createdAt) {
        this.createdAt = createdAt;
    }

    public PruebasEntity2() {
    }
}
