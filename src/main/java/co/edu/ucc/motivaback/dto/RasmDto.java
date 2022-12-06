package co.edu.ucc.motivaback.dto;

import com.google.cloud.firestore.annotation.PropertyName;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertDto
 */
public class RasmDto extends AbstractDto {
    private String idPoll;
    private String typeRasm;
    private int score;
    private String municipality;

    private String professional;
    private String nameBeneficiary;
    private String lastNameBeneficiary;
    private long identification;
    private String typeIdentification;
    private String typeQuestionnaire;
    private long cellphone;
    public String getTypeQuestionnaire() {
        return typeQuestionnaire;
    }

    public void setTypeQuestionnaire(String typeQuestionnaire) {
        this.typeQuestionnaire = typeQuestionnaire;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
    @PropertyName("id_poll")
    public String getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    @PropertyName("typeRasm")
    public String getTypeRasm() {
        return typeRasm;
    }

    @PropertyName("typeRasm")
    public void setTypeRasm(String typeRasm) {
        this.typeRasm = typeRasm;
    }
    @PropertyName("professional")
    public String getProfessional() {
        return professional;
    }

    @PropertyName("professional")
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

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(long cellphone) {
        this.cellphone = cellphone;
    }

}
