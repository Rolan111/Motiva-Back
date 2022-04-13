package co.edu.ucc.motivaback.entity;

public class RASMEntity extends AbstractEntity {
    private int id_poll;
    private String type_rasm;

    public int getId_poll() {
        return id_poll;
    }

    public void setId_poll(int id_poll) {
        this.id_poll = id_poll;
    }

    public String getType_rasm() {
        return type_rasm;
    }

    public void setType_rasm(String type_rasm) {
        this.type_rasm = type_rasm;
    }
}
