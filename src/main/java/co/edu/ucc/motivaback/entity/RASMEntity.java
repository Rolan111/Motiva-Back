package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class RASMEntity extends AbstractEntity {
    private String idPoll;
    private String typeRasm;
    private String professional;
    private String nameBeneficiary;
    private String lastNameBeneficiary;
    private long identification;
    private String typeIdentification;
    private long cellphone;

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

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(long cellphone) {
        this.cellphone = cellphone;
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

    @PropertyName("type_rasm")
    public String getTypeRasm() {
        return typeRasm;
    }

    @PropertyName("type_rasm")
    public void setTypeRasm(String typeRasm) {
        this.typeRasm = typeRasm;
    }
}
