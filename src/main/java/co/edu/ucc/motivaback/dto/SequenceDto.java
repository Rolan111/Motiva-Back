package co.edu.ucc.motivaback.dto;

public class SequenceDto {
    private Integer idAnswer;
    private Integer idPoll;

    public SequenceDto(Integer idAnswer, Integer idPoll) {
        this.idAnswer = idAnswer;
        this.idPoll = idPoll;
    }

    public Integer getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Integer idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Integer getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(Integer idPoll) {
        this.idPoll = idPoll;
    }
}
