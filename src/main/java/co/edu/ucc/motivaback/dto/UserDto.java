package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.UserRolEnum;
import com.google.gson.annotations.SerializedName;

public class UserDto extends AbstractDto {
    @SerializedName("job_profile")
    private UserRolEnum jobProfile;
    private String password;
    private String name;
    @SerializedName("last_name")
    private String lastName;
    private String identification;
    @SerializedName("identification_type")
    private String identificationType;
    @SerializedName("id_user")
    private Long idUser;
    @SerializedName("id_supervisor")
    private Long idSupervisor;

    public UserRolEnum getJobProfile() {
        return jobProfile;
    }

    public void setJobProfile(UserRolEnum jobProfile) {
        this.jobProfile = jobProfile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(Long idSupervisor) {
        this.idSupervisor = idSupervisor;
    }
}
