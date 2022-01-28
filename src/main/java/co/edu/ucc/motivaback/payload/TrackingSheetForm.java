package co.edu.ucc.motivaback.payload;

public class TrackingSheetForm {
    private String documentTrackingSheetId;
    private String names;
    private String lastnames;
    private String identification_type;
    private int n_identification;
    private String type_route;
    private String referred_entity;
    private String attention_status;
    private String recommendations_suggestions;

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

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public int getN_identification() {
        return n_identification;
    }

    public void setN_identification(int n_identification) {
        this.n_identification = n_identification;
    }

    public String getType_route() {
        return type_route;
    }

    public void setType_route(String type_route) {
        this.type_route = type_route;
    }

    public String getReferred_entity() {
        return referred_entity;
    }

    public void setReferred_entity(String referred_entity) {
        this.referred_entity = referred_entity;
    }

    public String getAttention_status() {
        return attention_status;
    }

    public void setAttention_status(String attention_status) {
        this.attention_status = attention_status;
    }

    public String getRecommendations_suggestions() {
        return recommendations_suggestions;
    }

    public void setRecommendations_suggestions(String recommendations_suggestions) {
        this.recommendations_suggestions = recommendations_suggestions;
    }
}
