package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;

public class PruebasEntity {

    private int createdBy;

    @PropertyName("created_by")
    public int getCreatedBy() {
        return createdBy;
    }

    @PropertyName("created_by")
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
