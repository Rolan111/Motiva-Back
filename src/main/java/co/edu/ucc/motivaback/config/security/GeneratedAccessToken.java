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
    private String fullName;
    private String rol;

    public GeneratedAccessToken(String token, TokenTypeEnum type, Date expirationDate, String username, String fullName, String rol) {
        this.token = token;
        this.type = type;
        this.expirationDate = expirationDate;
        this.username = username;
        this.fullName = fullName;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
