package co.edu.ucc.motivaback.dto;

import com.google.gson.annotations.SerializedName;

public class RepComAgentDto extends AbstractDto {
    @SerializedName("activity_name")
    private String activityName;
    @SerializedName("activity_number")
    private int activityNumber;
    private String date;
    private String duration;
    private String place;
    @SerializedName("number_attendees")
    private int numberAttendees;
    @SerializedName("activity_objectives")
    private String activityObjectives;
    @SerializedName("resources_used")
    private String resourcesUsed;
    @SerializedName("methodology_used")
    private String methodologyUsed;
    @SerializedName("activity_description_development")
    private String activityDescriptionDevelopment;
    @SerializedName("resources_obtained")
    private String resourcesObtained;
    private String evidence;
    @SerializedName("activity_professional_incharge")
    private String activityProfessionalIncharge;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getNumberAttendees() {
        return numberAttendees;
    }

    public void setNumberAttendees(int numberAttendees) {
        this.numberAttendees = numberAttendees;
    }

    public String getActivityObjectives() {
        return activityObjectives;
    }

    public void setActivityObjectives(String activityObjectives) {
        this.activityObjectives = activityObjectives;
    }

    public String getResourcesUsed() {
        return resourcesUsed;
    }

    public void setResourcesUsed(String resourcesUsed) {
        this.resourcesUsed = resourcesUsed;
    }

    public String getMethodologyUsed() {
        return methodologyUsed;
    }

    public void setMethodologyUsed(String methodologyUsed) {
        this.methodologyUsed = methodologyUsed;
    }

    public String getActivityDescriptionDevelopment() {
        return activityDescriptionDevelopment;
    }

    public void setActivityDescriptionDevelopment(String activityDescriptionDevelopment) {
        this.activityDescriptionDevelopment = activityDescriptionDevelopment;
    }

    public String getResourcesObtained() {
        return resourcesObtained;
    }

    public void setResourcesObtained(String resourcesObtained) {
        this.resourcesObtained = resourcesObtained;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getActivityProfessionalIncharge() {
        return activityProfessionalIncharge;
    }

    public void setActivityProfessionalIncharge(String activityProfessionalIncharge) {
        this.activityProfessionalIncharge = activityProfessionalIncharge;
    }
}
