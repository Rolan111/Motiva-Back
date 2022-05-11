package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class InactiveAlertEntity extends AbstractEntity {
    private int idPoll;

    @PropertyName("id_poll")
    public int getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(int idPoll) {
        this.idPoll = idPoll;
    }
}
