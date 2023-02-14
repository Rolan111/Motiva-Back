package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class InactiveAlertEntity extends AlertEntity {

    private String reason;

    @PropertyName("reason")
    public String getReason() {
        return reason;
    }
    @PropertyName("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

}
