package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class AnswerQuantitativeInstrumentForm
 */
public class AnswerQuantitativeInstrumentDto {
    private Long idAnswer;
    private Long idQuestion;
    private Long idOptionAnswer;
    private String openAnswer;
    private Long idPoll;

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
}
