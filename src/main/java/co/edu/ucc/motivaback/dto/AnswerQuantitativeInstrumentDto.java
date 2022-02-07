package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerQuantitativeInstrumentForm
 */
public class AnswerQuantitativeInstrumentDto {
    @SerializedName("id_answer")
    private Long idAnswer;
    @SerializedName("id_question")
    private Long idQuestion;
    @SerializedName("id_option_answer")
    private Long idOptionAnswer;
    @SerializedName("open_answer")
    private String openAnswer;
    @SerializedName("id_poll")
    private Long idPoll;
    @SerializedName("multiple_answer")
    private List<Long> multipleAnswer;

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

    public Long getIdOptionAnswer() {
        return idOptionAnswer;
    }

    public void setIdOptionAnswer(Long idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
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

    public List<Long> getMultipleAnswer() {
        return multipleAnswer;
    }

    public void setMultipleAnswer(List<Long> multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }
}
