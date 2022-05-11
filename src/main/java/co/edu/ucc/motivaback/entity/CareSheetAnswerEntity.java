package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

import java.util.List;

public class CareSheetAnswerEntity extends AbstractEntity {

    private int idAnswer;
    private int idQuestion;
    private List<Integer> idOptionAnswers;
    private String openAnswer;
    private int idPoll;

    @PropertyName("id_answer")
    public int getIdAnswer() {
        return idAnswer;
    }

    @PropertyName("id_answer")
    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    @PropertyName("id_question")
    public int getIdQuestion() {
        return idQuestion;
    }

    @PropertyName("id_question")
    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    @PropertyName("id_option_answers")
    public List<Integer> getIdOptionAnswers() {
        return idOptionAnswers;
    }

    @PropertyName("id_option_answers")
    public void setIdOptionAnswers(List<Integer> idOptionAnswers) {
        this.idOptionAnswers = idOptionAnswers;
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
    public int getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(int idPoll) {
        this.idPoll = idPoll;
    }
}
