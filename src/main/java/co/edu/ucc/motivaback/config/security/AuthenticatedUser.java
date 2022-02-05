package co.edu.ucc.motivaback.config.security;

public class AuthenticatedUser {
    private String id;
    private String rol;

    public AuthenticatedUser(String id, String rol) {
        this.id = id;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
