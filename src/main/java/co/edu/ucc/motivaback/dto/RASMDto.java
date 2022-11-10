package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertDto
 */
public class RASMDto extends AbstractDto {
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
    private String date;
    private long cellphone;

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(long cellphone) {
        this.cellphone = cellphone;
    }

    public String getLastNameBeneficiary() {
        return lastNameBeneficiary;
    }

    public void setLastNameBeneficiary(String lastNameBeneficiary) {
        this.lastNameBeneficiary = lastNameBeneficiary;
    }

    public String getNameProfessional() {
        return professional;
    }

    public void setNameProfessional(String nameProfessional) {
        this.professional = nameProfessional;
    }


    public String getNameBeneficiary() {
        return nameBeneficiary;
    }

    public void setNameBeneficiary(String nameBeneficiary) {
        this.nameBeneficiary = nameBeneficiary;
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
