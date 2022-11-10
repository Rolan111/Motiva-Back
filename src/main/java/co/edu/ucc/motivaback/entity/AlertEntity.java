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

    private String nameBeneficiary; private String professional;
    private String lastNameBeneficiary;
    private long identification;
    private String typeIdentification;
    private String municipality;
    private String date;

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getNameBeneficiary() {
        return nameBeneficiary;
    }

    public void setNameBeneficiary(String nameBeneficiary) {
        this.nameBeneficiary = nameBeneficiary;
    }

    public String getLastNameBeneficiary() {
        return lastNameBeneficiary;
    }

    public void setLastNameBeneficiary(String lastNameBeneficiary) {
        this.lastNameBeneficiary = lastNameBeneficiary;
    }

    public long getIdentification() {
        return identification;
    }

    public void setIdentification(long identification) {
        this.identification = identification;
    }

    public String getTypeIdentification() {
        return typeIdentification;
    }

    public void setTypeIdentification(String typeIdentification) {
        this.typeIdentification = typeIdentification;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
