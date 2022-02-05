package co.edu.ucc.motivaback.config.security;

public class AuthenticatedUser {
    private String idFireBase;
    private Integer id;
    private String rol;

    public AuthenticatedUser(String idFireBase, String rol, Integer id) {
        this.idFireBase = idFireBase;
        this.rol = rol;
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getIdFireBase() {
        return idFireBase;
    }

    public void setIdFireBase(String idFireBase) {
        this.idFireBase = idFireBase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
