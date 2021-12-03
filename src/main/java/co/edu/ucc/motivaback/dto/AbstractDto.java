package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.log.DadLog;

import java.util.Date;

public abstract class AbstractDto {

    private String id;

    private Long createdBy;

    private Date createdAt;

    private Long updatedBy;

    private Date updatedAt;

    private RegisterStatusEnum status;

    public AbstractDto() {
        setCreatedAt(new Date());
        setCreatedBy(1L);
        setStatus(RegisterStatusEnum.ACTIVE);
    }

    public AbstractDto(RegisterStatusEnum status) {
        this.status = status;
    }

    public AbstractDto(DadLog dadLog) {
        this.createdAt = dadLog.getCreatedAt();
        this.createdBy = dadLog.getCreatedBy();
        this.updatedAt = dadLog.getUpdatedAt();
        this.updatedBy = dadLog.getUpdatedBy();
        this.status = dadLog.getStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

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
