package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class PruebasEntity {

    private int createdBy;

    private String createdAt;

    @PropertyName("created_by")
    public int getCreatedBy() {
        return createdBy;
    }

    @PropertyName("created_by")
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

//    @PropertyName("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

//    @PropertyName("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PruebasEntity(int createdBy, String createdAt) { //Agregar constructor para poder agregar datos
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public PruebasEntity() { //Constructor vacío necesario porque sino da error y que sea público

    }
}
