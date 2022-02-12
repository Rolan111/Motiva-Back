package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;

import java.util.Date;

public abstract class AbstractEntity {
    @DocumentId
    private String id;
    private Long createdBy;
    private Date createdAt;
    private Long updatedBy;
    private Date updatedAt;
    private RegisterStatusEnum status;

    protected AbstractEntity() {
        setCreatedAt(new Date());
        setCreatedBy(1L);
        setStatus(RegisterStatusEnum.ACTIVE);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("created_by")
    public Long getCreatedBy() {
        return createdBy;
    }

    @PropertyName("created_by")
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @PropertyName("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @PropertyName("created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PropertyName("updated_by")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    @PropertyName("updated_by")
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PropertyName("updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @PropertyName("updated_at")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RegisterStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RegisterStatusEnum status) {
        this.status = status;
    }
}
