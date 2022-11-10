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
    private String professional;
    private String nameBeneficiary;
    private String lastNameBeneficiary;
    private long identification;
    private String typeIdentification;
    private String municipality;
    private String questionnaireType;
    private String date;
    private long cellphone;

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(long cellphone) {
        this.cellphone = cellphone;
    }
    @PropertyName("last_name_beneficiary")
    public String getLastNameBeneficiary() {
        return lastNameBeneficiary;
    }
    @PropertyName("last_name_beneficiary")
    public void setLastNameBeneficiary(String lastNameBeneficiary) {
        this.lastNameBeneficiary = lastNameBeneficiary;
    }

    public String getNameProfessional() {
        return professional;
    }

    public void setNameProfessional(String nameProfessional) {
        this.professional = nameProfessional;
    }

    @PropertyName("name_beneficiary")
    public String getNameBeneficiary() {
        return nameBeneficiary;
    }
    @PropertyName("name_beneficiary")
    public void setNameBeneficiary(String nameBeneficiary) {
        this.nameBeneficiary = nameBeneficiary;
    }


    public long getIdentification() {
        return identification;
    }

    public void setIdentification(long identification) {
        this.identification = identification;
    }
    @PropertyName("type_identification")
    public String getTypeIdentification() {
        return typeIdentification;
    }
    @PropertyName("type_identification")
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

//    public int getIdPoll() {
//        return idPoll;
//    }
//
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

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }
    @PropertyName("questionnaire_type")
    public String getQuestionnaireType() {
        return questionnaireType;
    }
    @PropertyName("questionnaire_type")
    public void setQuestionnaireType(String questionnaireType) {
        this.questionnaireType = questionnaireType;
    }
}
