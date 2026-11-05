package com.generation.tienda3v.controllers;

import com.generation.tienda3v.dto.LoginRequest;
import com.generation.tienda3v.dto.TokenJwt;
import com.generation.tienda3v.services.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController { //Este controlador recibirá el correo y la contraseña, verificará las credenciales y devolverá el token JWT.
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }//Constructor

    @PostMapping("/login") // Endpoint para iniciar sesión
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validamos correo y contraseña
        if (!jwtService.validarUsuario(request)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }//if usuario

        String token = jwtService.generarToken(request.getCorreo());// Generamos el token
        return ResponseEntity.ok(new TokenJwt(token)); // Devolvemos el token al usuario
    }//login
}//class AuthController