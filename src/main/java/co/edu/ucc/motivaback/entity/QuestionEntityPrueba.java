package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class QuestionEntityPrueba extends AbstractEntity {
    private int idQuetion;
    private String description;

    @PropertyName("id_quetion")
    public int getIdQuetion() {
        return idQuetion;
    }

    @PropertyName("id_quetion")
    public void setIdQuetion(int idQuetion) {
        this.idQuetion = idQuetion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
