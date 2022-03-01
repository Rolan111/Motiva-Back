package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.dto.CommentsDto;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Map;

@Document(collectionName = "tracking_sheet")
public class TrackingSheetEntity extends AbstractEntity {

    private String idTrackingSheet;

    private String names;
    private String lastnames;
    private String identificationType;
    private String identification;
    private String typeRoute;
    private String referredEntity;
    private String attentionStatus;
    private String recommendations;
    private Map<String, String> comments;

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public String getIdTrackingSheet() {
        return idTrackingSheet;
    }

    public void setIdTrackingSheet(String idTrackingSheet) {
        this.idTrackingSheet = idTrackingSheet;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastnames() {
        return lastnames;
    }

    public void setLastnames(String lastnames) {
        this.lastnames = lastnames;
    }

    @PropertyName("identification_type")
    public String getIdentificationType() {
        return identificationType;
    }

    @PropertyName("identification_type")
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @PropertyName("type_route")
    public String getTypeRoute() {
        return typeRoute;
    }

    @PropertyName("type_route")
    public void setTypeRoute(String typeRoute) {
        this.typeRoute = typeRoute;
    }

    @PropertyName("refered_entity")
    public String getReferredEntity() {
        return referredEntity;
    }

    @PropertyName("refered_entity")
    public void setReferredEntity(String referredEntity) {
        this.referredEntity = referredEntity;
    }

    @PropertyName("attention_status")
    public String getAttentionStatus() {
        return attentionStatus;
    }

    @PropertyName("attention_status")
    public void setAttentionStatus(String attentionStatus) {
        this.attentionStatus = attentionStatus;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
}
