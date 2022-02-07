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
    private Integer idAnswer;
    @SerializedName("id_question")
    private Integer idQuestion;
    @SerializedName("id_option_answer")
    private Integer idOptionAnswer;
    @SerializedName("open_answer")
    private String openAnswer;
    @SerializedName("id_poll")
    private Integer idPoll;
    @SerializedName("multiple_answer")
    private List<Integer> multipleAnswer;

    public Integer getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Integer idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Integer getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Integer getIdOptionAnswer() {
        return idOptionAnswer;
    }

    public void setIdOptionAnswer(Integer idOptionAnswer) {
        this.idOptionAnswer = idOptionAnswer;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }

    public Integer getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(Integer idPoll) {
        this.idPoll = idPoll;
    }

    public List<Integer> getMultipleAnswer() {
        return multipleAnswer;
    }

    public void setMultipleAnswer(List<Integer> multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }
}
