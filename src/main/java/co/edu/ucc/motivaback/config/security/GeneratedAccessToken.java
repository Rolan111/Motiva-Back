package co.edu.ucc.motivaback.config.security;

import co.edu.ucc.motivaback.enums.TokenTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class GeneratedAccessToken {
    private String token;
    private TokenTypeEnum type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSz")
    private Date expirationDate;
    private String username;
    private String name;
    private String last_name;
    private String job_profile;

    public GeneratedAccessToken(String token, TokenTypeEnum type, Date expirationDate, String username, String name, String last_name, String job_profile) {
        this.token = token;
        this.type = type;
        this.expirationDate = expirationDate;
        this.username = username;
        this.name = name;
        this.last_name = last_name;
        this.job_profile = job_profile;
    }

    public String getJob_profile() {
        return job_profile;
    }

    public void setJob_profile(String job_profile) {
        this.job_profile = job_profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenTypeEnum getType() {
        return type;
    }

    public void setType(TokenTypeEnum type) {
        this.type = type;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
