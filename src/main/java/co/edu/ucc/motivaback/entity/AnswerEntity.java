package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerEntity
 */
@Document(collectionName = "answer")
public class AnswerEntity extends AbstractEntity {
    private Long idAnswer;
    private Long idQuestion;
    private String openAnswer;
    private Long idPoll;
    private List<Long> idOptionAnswers;

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
    public Long getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(Long idPoll) {
        this.idPoll = idPoll;
    }

    @PropertyName("id_option_answers")
    public List<Long> getIdOptionAnswers() {
        return idOptionAnswers;
    }

    @PropertyName("id_option_answers")
    public void setIdOptionAnswers(List<Long> idOptionAnswers) {
        this.idOptionAnswers = idOptionAnswers;
    }
}
