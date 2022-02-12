package co.edu.ucc.motivaback.entity;

import co.edu.ucc.motivaback.enums.UserRolEnum;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Document(collectionName = "user")
public class UserEntity extends AbstractEntity implements UserDetails {
    private UserRolEnum jobProfile;
    private String password;
    private String name;
    private String lastName;
    private String identification;
    private String identificationType;
    private Long idUser;
    private Long idSupervisor;

    @PropertyName("job_profile")
    public UserRolEnum getJobProfile() {
        return jobProfile;
    }

    @PropertyName("job_profile")
    public void setJobProfile(UserRolEnum jobProfile) {
        this.jobProfile = jobProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(jobProfile.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.identification;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    @PropertyName("last_name")
    public String getLastName() {
        return lastName;
    }

    @PropertyName("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @PropertyName("identification_type")
    public String getIdentificationType() {
        return identificationType;
    }

    @PropertyName("identification_type")
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    @PropertyName("id_user")
    public Long getIdUser() {
        return idUser;
    }

    @PropertyName("id_user")
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @PropertyName("id_supervisor")
    public Long getIdSupervisor() {
        return idSupervisor;
    }

    @PropertyName("id_supervisor")
    public void setIdSupervisor(Long idSupervisor) {
        this.idSupervisor = idSupervisor;
    }
}

