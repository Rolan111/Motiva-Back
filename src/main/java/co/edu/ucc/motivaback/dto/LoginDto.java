package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class LoginDto
 */

public class LoginDto {
    private String userId;
    private String username;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
