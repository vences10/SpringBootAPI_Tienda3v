package com.generation.tienda3v.dto;

public class LoginRequest { //recibe las credenciales del usuario.
    private String correo;
    private String password;
    public LoginRequest() {} //constructor vacio

    public String getCorreo() {
        return correo;
    }//getCorreo

    public void setCorreo(String correo) {
        this.correo = correo;
    }//setCorreo

    public String getPassword() {
        return password;
    }//getPassword

    public void setPassword(String password) {
        this.password = password;
    }//setPassword
}//class LoginRequets