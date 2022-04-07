package co.edu.ucc.motivaback.entity;

import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "alerts")
public class AlertsEntity extends AbstractEntity {
    private int id_alert;
    private int id_poll;

    public int getId_alert() {
        return id_alert;
    }

    public void setId_alert(int id_alert) {
        this.id_alert = id_alert;
    }

    public int getId_poll() {
        return id_poll;
    }

    public void setId_poll(int id_poll) {
        this.id_poll = id_poll;
    }
}
