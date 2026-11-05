package com.generation.tienda3v.services;

import com.generation.tienda3v.config.JwtFilter;
import com.generation.tienda3v.dto.LoginRequest;
import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.repositories.UsuarioRepository;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Autowired
    private PasswordEncoder encoder;
    private final UsuarioRepository usuarioRepository;

    public JwtService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean validarUsuario(LoginRequest request) { // Verificamos que el usuario exista y que su contraseña coincida
        if (request.getCorreo() == null || request.getPassword() == null ||
                request.getCorreo().isBlank() || request.getPassword().isBlank()) { // Validamos que los datos no estén vacíos
            return false;
        }
        String correo = request.getCorreo().trim().toLowerCase(); // Normalizamos el correo

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo); // Buscamos al usuario
        if (usuario.isEmpty()) {
            return false;
        }
        Usuario usr = usuario.get();
        if (Boolean.FALSE.equals(usr.getActivo())) {
            return false;
        } // Verificamos que la cuenta esté activa



        boolean coincide = encoder.matches(
                request.getPassword(),
                usr.getPasswordHash()
        );



        return coincide;

    }//validarUsuario

    public String generarToken(String correo) { // Generamos el token JWT
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 2);
        return Jwts.builder()
                .subject(correo)
                .issuedAt(new Date())
                .expiration(calendar.getTime())
                .signWith(JwtFilter.getSignInKey())
                .compact();
    }//generarTokens
}//class JwtService