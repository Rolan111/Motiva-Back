package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.TypeQuestionaryEnum;

import java.util.Date;

/**
 * @author dsolano
 * @project motiva-back
 * @class PollDto
 */
public class PollDto extends AbstractDto {

    private String idPoll;
    private String approvalDoc;
    private Date date;
    private String evidence;
    private Integer idUser;
    private TypeQuestionaryEnum type;
    private Integer idCity;
    private CityDto cityDto;

    public Integer getIdCity() {
        return idCity;
    }

    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(String idPoll) {
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

    public CityDto getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDto cityDto) {
        this.cityDto = cityDto;
    }

    public TypeQuestionaryEnum getType() {
        return type;
    }

    public void setType(TypeQuestionaryEnum type) {
        this.type = type;
    }
}
