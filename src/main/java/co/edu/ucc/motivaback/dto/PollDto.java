package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author dsolano
 * @project motiva-back
 * @class PollDto
 */
public class PollDto extends AbstractDto {
    private Integer idPoll;
    @SerializedName("approval_doc")
    private String approvalDoc;
    private Date date;
    private String evidence;
    @SerializedName("id_user")
    private Integer idUser;

    public Integer getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(Integer idPoll) {
        this.idPoll = idPoll;
    }

    public String getApprovalDoc() {
        return approvalDoc;
    }

    public void setApprovalDoc(String approvalDoc) {
        this.approvalDoc = approvalDoc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
