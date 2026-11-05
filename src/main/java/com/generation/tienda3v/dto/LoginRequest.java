package com.generation.tienda3v.dto;

public class LoginRequest { //recibe las credenciales del usuario.
    private String correo;
    private String password;
    private String passwordHash;

    public LoginRequest() {} //constructor vacio

    public String getCorreo() {
        return correo;
    }//getCorreo

    public void setCorreo(String correo) {
        this.correo = correo;
    }//setCorreo

    public String getPassword() {
        return (password != null && !password.isBlank()) ? password : passwordHash;
    }//getPassword

    public void setPassword(String password) {
        this.password = password;
    }//setPassword

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}//class LoginRequets
