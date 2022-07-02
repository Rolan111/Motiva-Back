package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class RASMEntity extends AbstractEntity {
    private String idPoll;
    private String typeRasm;

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
