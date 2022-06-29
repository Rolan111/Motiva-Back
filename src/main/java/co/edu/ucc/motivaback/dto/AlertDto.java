package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertDto
 */
public class AlertDto extends AbstractDto {
    private int idAlert;
//    private int idPoll;
    private String idPoll;
    private int score;

    public int getIdAlert() {
        return idAlert;
    }

    public void setIdAlert(int idAlert) {
        this.idAlert = idAlert;
    }

//    public int getIdPoll() {
//        return idPoll;
//    }
//
//    public void setIdPoll(int idPoll) {
//        this.idPoll = idPoll;
//    }


    public String getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
