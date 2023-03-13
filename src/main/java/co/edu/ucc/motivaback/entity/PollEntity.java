package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;
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
//    private Integer idPoll;
    private String idPoll;
    private String approvalDoc;
    private Date date;
    private String evidence;
    private Integer idUser;
    private TypeQuestionaryEnum type;
    private Integer idCity;

    @PropertyName("id_poll")
    public String getIdPoll() {
        return idPoll;
    }

    @PropertyName("id_poll")
    public void setIdPoll(String idPoll) {
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

    @PropertyName("id_city")
    public Integer getIdCity() {
        return idCity;
    }

    @PropertyName("id_city")
    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }
}
