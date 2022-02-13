package co.edu.ucc.motivaback.dto;

import co.edu.ucc.motivaback.enums.UserRolEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto extends AbstractDto {
    private UserRolEnum jobProfile;
    private String name;
    private String lastName;
    private String identification;
    private String identificationType;
    private Long idUser;
    private Long idSupervisor;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserRolEnum getJobProfile() {
        return jobProfile;
    }

    public void setJobProfile(UserRolEnum jobProfile) {
        this.jobProfile = jobProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
