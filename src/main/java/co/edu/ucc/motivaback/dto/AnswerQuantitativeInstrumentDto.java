package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerQuantitativeInstrumentForm
 */
public class AnswerQuantitativeInstrumentDto {
    private Integer idAnswer;
    private Integer idQuestion;
    private Integer idOptionAnswer;
    private String openAnswer;
    private Integer idPoll;

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
}
