package org.utl.integradora.model;

public class Usuario {

    private int idUsuario;
    private String user;
    private String contrasenia;
    private String lastConnect;
    private String token;

    public Usuario(int idUsuario, String user, String contrasenia, String lastConnect, String token) {
        this.idUsuario = idUsuario;
        this.user = user;
        this.contrasenia = contrasenia;
        this.lastConnect = lastConnect;
        this.token = token;
    }

    public Usuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getLastConnect() {
        return lastConnect;
    }

    public void setLastConnect(String lastConnect) {
        this.lastConnect = lastConnect;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}