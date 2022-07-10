package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertEntity
 */
@Document(collectionName = "alert")
public class AlertEntity extends AbstractEntity {
    private int idAlert;
//    private int idPoll;
    private String idPoll;
    private int score;

    @PropertyName("id_alert")
    public int getIdAlert() {
        return idAlert;
    }

    @PropertyName("id_alert")
    public void setIdAlert(int idAlert) {
        this.idAlert = idAlert;
    }

//    @PropertyName("id_poll")
//    public int getIdPoll() {
//        return idPoll;
//    }
//
//    @PropertyName("id_poll")
//    public void setIdPoll(int idPoll) {
//        this.idPoll = idPoll;
//    }

    @PropertyName("id_poll")
    public String getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
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
