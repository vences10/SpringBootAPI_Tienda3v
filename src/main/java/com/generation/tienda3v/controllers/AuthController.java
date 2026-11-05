package com.generation.tienda3v.controllers;

import com.generation.tienda3v.dto.LoginRequest;
import com.generation.tienda3v.dto.TokenJwt;
import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.services.JwtService;
import com.generation.tienda3v.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController { //Este controlador recibirá el correo y la contraseña, verificará las credenciales y devolverá el token JWT.
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }//Constructor

    @PostMapping("/login") // Endpoint para iniciar sesión
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validamos correo y contraseña
        if (!jwtService.validarUsuario(request)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }//if usuario

        String correoNormalizado = request.getCorreo().trim().toLowerCase();
        String token = jwtService.generarToken(correoNormalizado);// Generamos el token
        Usuario usuario = usuarioService.obtenerPorCorreo(correoNormalizado);
        return ResponseEntity.ok(new TokenJwt(token, usuario)); // Devolvemos el token y datos del usuario
    }//login
}//class AuthController

