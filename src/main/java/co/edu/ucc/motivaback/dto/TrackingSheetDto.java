package co.edu.ucc.motivaback.dto;

public class TrackingSheetDto {
    private String documentTrackingSheetId;
    private String names;
    private String lastnames;
    private String identificationType;
    private int nIdentification;
    private String typeRoute;
    private String referredEntity;
    private String attentionStatus;
    private String recommendationsSuggestions;

    public String getDocumentTrackingSheetId() {
        return documentTrackingSheetId;
    }

    public void setDocumentTrackingSheetId(String documentTrackingSheetId) {
        this.documentTrackingSheetId = documentTrackingSheetId;
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

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public int getnIdentification() {
        return nIdentification;
    }

    public void setnIdentification(int nIdentification) {
        this.nIdentification = nIdentification;
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

    public String getRecommendationsSuggestions() {
        return recommendationsSuggestions;
    }

    public void setRecommendationsSuggestions(String recommendationsSuggestions) {
        this.recommendationsSuggestions = recommendationsSuggestions;
    }
}
