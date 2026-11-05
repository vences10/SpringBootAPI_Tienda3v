package com.generation.tienda3v.dto;

public class TokenJwt { //devuelve el token generado y el tipo de autenticación (Bearer).
    private String token;
    private String tipo = "Bearer";

    public TokenJwt(String token) {
        this.token = token;
    }//constructor

    public String getToken() {
        return token;
    }//getToken

    public String getTipo() {
        return tipo;
    }//getTipo
}//class TokenJwt