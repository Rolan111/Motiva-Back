package co.edu.ucc.motivaback.config.security;

public class AuthenticatedUser {
    private String username;
    private String rol;

    public AuthenticatedUser(String username, String rol) {
        this.username = username;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
