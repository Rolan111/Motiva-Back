package co.edu.ucc.motivaback.dto;

import com.google.cloud.firestore.annotation.PropertyName;

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
    private String nameProfessional;
    private String lastNameProfessional;
    private String nameBeneficiary;
    private String lastNameBeneficiary;
    private int identification;
    private String typeIdentification;
    private String municipality;
    private String date;

    public String getLastNameBeneficiary() {
        return lastNameBeneficiary;
    }

    public void setLastNameBeneficiary(String lastNameBeneficiary) {
        this.lastNameBeneficiary = lastNameBeneficiary;
    }

    public String getNameProfessional() {
        return nameProfessional;
    }

    public void setNameProfessional(String nameProfessional) {
        this.nameProfessional = nameProfessional;
    }

    public String getLastNameProfessional() {
        return lastNameProfessional;
    }

    public void setLastNameProfessional(String lastNameProfessional) {
        this.lastNameProfessional = lastNameProfessional;
    }

    public String getNameBeneficiary() {
        return nameBeneficiary;
    }

    public void setNameBeneficiary(String nameBeneficiary) {
        this.nameBeneficiary = nameBeneficiary;
    }


    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
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
