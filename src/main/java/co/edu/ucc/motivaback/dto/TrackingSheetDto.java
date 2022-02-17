package co.edu.ucc.motivaback.dto;

public class TrackingSheetDto extends AbstractDto {

    private int idTrackingSheet;
    private String names;
    private String lastnames;
    private String identificationType;
    private String identification;
    private String typeRoute;
    private String referredEntity;
    private String attentionStatus;
    private String recommendations;

    public int getIdTrackingSheet() {
        return idTrackingSheet;
    }

    public void setIdTrackingSheet(int idTrackingSheet) {
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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getTypeRoute() {
        return typeRoute;
    }

    public void setTypeRoute(String typeRoute) {
        this.typeRoute = typeRoute;
    }

    public String getReferredEntity() {
        return referredEntity;
    }

    public void setReferredEntity(String referredEntity) {
        this.referredEntity = referredEntity;
    }

    public String getAttentionStatus() {
        return attentionStatus;
    }

    public void setAttentionStatus(String attentionStatus) {
        this.attentionStatus = attentionStatus;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }
}
