package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

import java.util.Date;

/**
 * @author dsolano
 * @project motiva-back
 * @class PollDto
 */
@Document(collectionName = "poll")
public class PollEntity extends AbstractEntity {
    private Integer idPoll;
    private String approvalDoc;
    private Date date;
    private String evidence;
    private Integer idUser;

    @PropertyName("id_poll")
    public Integer getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(Integer idPoll) {
        this.idPoll = idPoll;
    }

    @PropertyName("approval_doc")
    public String getApprovalDoc() {
        return approvalDoc;
    }

    @PropertyName("approval_doc")
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

    @PropertyName("id_user")
    public Integer getIdUser() {
        return idUser;
    }

    @PropertyName("id_user")
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
