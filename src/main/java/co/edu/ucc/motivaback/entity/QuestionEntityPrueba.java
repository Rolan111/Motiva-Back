package co.edu.ucc.motivaback.entity;

public class QuestionEntityPrueba extends AbstractEntity {
    private int id_quetion;
    private String description;

    public int getId_quetion() {
        return id_quetion;
    }

    public void setId_quetion(int id_quetion) {
        this.id_quetion = id_quetion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
