package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerQuantitativeInstrumentForm
 */
public class AnswerDto {
    @SerializedName("id_answer")
    private Long idAnswer;
    @SerializedName("id_question")
    private Long idQuestion;
    @SerializedName("open_answer")
    private String openAnswer;
    @SerializedName("id_poll")
    private Long idPoll;
    @SerializedName("id_option_answers")
    private List<Long> idOptionAnswers;

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

    public Long getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(Long idPoll) {
        this.idPoll = idPoll;
    }

    public List<Long> getIdOptionAnswers() {
        return idOptionAnswers;
    }

    public void setIdOptionAnswers(List<Long> idOptionAnswers) {
        this.idOptionAnswers = idOptionAnswers;
    }
}
