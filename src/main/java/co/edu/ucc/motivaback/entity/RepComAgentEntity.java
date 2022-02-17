package co.edu.ucc.motivaback.entity;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "rep_com_agent")
public class RepComAgentEntity extends AbstractEntity {

    private int idRepComAgent;
    private String activityName;
    private int activityNumber;
    private String date;
    private String duration;
    private String place;
    private int numberAttendees;
    private String activityObjectives;
    private String resourcesUsed;
    private String methodologyUsed;
    private String activityDescriptionDevelopment;
    private String resourcesObtained;
    private String evidence;
    private String activityProfessionalIncharge;

    public int getIdRepComAgent() {
        return idRepComAgent;
    }

    public void setIdRepComAgent(int idRepComAgent) {
        this.idRepComAgent = idRepComAgent;
    }

    @PropertyName("activity_name")
    public String getActivityName() {
        return activityName;
    }

    @PropertyName("activity_name")
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @PropertyName("activity_number")
    public int getActivityNumber() {
        return activityNumber;
    }

    @PropertyName("activity_number")
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

    @PropertyName("number_attendees")
    public int getNumberAttendees() {
        return numberAttendees;
    }

    @PropertyName("number_attendees")
    public void setNumberAttendees(int numberAttendees) {
        this.numberAttendees = numberAttendees;
    }

    @PropertyName("activity_objectives")
    public String getActivityObjectives() {
        return activityObjectives;
    }

    @PropertyName("activity_objectives")
    public void setActivityObjectives(String activityObjectives) {
        this.activityObjectives = activityObjectives;
    }

    @PropertyName("resources_used")
    public String getResourcesUsed() {
        return resourcesUsed;
    }

    @PropertyName("resources_used")
    public void setResourcesUsed(String resourcesUsed) {
        this.resourcesUsed = resourcesUsed;
    }

    @PropertyName("methodology_used")
    public String getMethodologyUsed() {
        return methodologyUsed;
    }

    @PropertyName("methodology_used")
    public void setMethodologyUsed(String methodologyUsed) {
        this.methodologyUsed = methodologyUsed;
    }

    @PropertyName("activity_description_development")
    public String getActivityDescriptionDevelopment() {
        return activityDescriptionDevelopment;
    }

    @PropertyName("activity_description_development")
    public void setActivityDescriptionDevelopment(String activityDescriptionDevelopment) {
        this.activityDescriptionDevelopment = activityDescriptionDevelopment;
    }

    @PropertyName("resources_obtained")
    public String getResourcesObtained() {
        return resourcesObtained;
    }

    @PropertyName("resources_obtained")
    public void setResourcesObtained(String resourcesObtained) {
        this.resourcesObtained = resourcesObtained;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    @PropertyName("activity_professional_incharge")
    public String getActivityProfessionalIncharge() {
        return activityProfessionalIncharge;
    }

    @PropertyName("activity_professional_incharge")
    public void setActivityProfessionalIncharge(String activityProfessionalIncharge) {
        this.activityProfessionalIncharge = activityProfessionalIncharge;
    }

}
