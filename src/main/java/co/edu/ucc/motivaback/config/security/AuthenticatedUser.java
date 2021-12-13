package co.edu.ucc.motivaback.config.security;

public class AuthenticatedUser {
    private Long userId;
    private String username;
    private String rol;

    public AuthenticatedUser(Long userId, String username, String rol) {
        this.userId = userId;
        this.username = username;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
