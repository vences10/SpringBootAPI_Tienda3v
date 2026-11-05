package com.generation.tienda3v.dto;

import com.generation.tienda3v.models.Usuario;

public class TokenJwt { //devuelve el token generado y el tipo de autenticación (Bearer).
    private String token;
    private String tipo = "Bearer";
    private Usuario usuario;

    public TokenJwt(String token) {
        this.token = token;
    }//constructor

    public TokenJwt(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }//getToken

    public String getTipo() {
        return tipo;
    }//getTipo

    public Usuario getUsuario() {
        return usuario;
    }
}//class TokenJwt
