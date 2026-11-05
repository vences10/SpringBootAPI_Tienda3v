package com.generation.tienda3v.config;

import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.repositories.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class JwtFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    public JwtFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }//jwtFilter

    // Clave secreta para firmar y validar los tokens
    // String secret = System.getenv("JWT_SECRET");

    public static SecretKey getSignInKey() {

        String secret =
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

        byte[] keyBytes = Base64.getDecoder().decode(secret);

        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }//getSignKey

    /*
    *1. Generar tu nueva clave secreta: Abre PowerShell y ejecuta:
    * [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
    * Obtendrás una cadena larga. Cópiala y guárdala en un lugar privado.
    * 2. Configurarla en IntelliJ: Entra a Run → Edit Configurations.
    * 3. Selecciona la configuración de tu aplicación Spring Boot.
    * 4. Busca el apartado Environment variables.
    * 5. Agrega esta variable:
    * - Name: JWT_SECRET   - Value: pega la clave que generaste.
    * 6. Presiona Apply y después OK.
     */

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no existe el token, continuamos. Spring Security decidirá si la ruta permite acceso.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }//if token Exist
        String token = authHeader.substring(7);
        try { // Validamos el token y recuperamos sus datos
            Claims claims = Jwts.parser()
                    .verifyWith(JwtFilter.getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String correo = claims.getSubject();

            Optional<Usuario> usuarioOptional = // Buscamos al usuario en la base de datos
                    usuarioRepository.findByCorreo(correo);
            if (usuarioOptional.isEmpty() ||
                    Boolean.FALSE.equals(usuarioOptional.get().getActivo())) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado o cuenta inactiva"
                );
                return;
            }//validarToken

            Usuario usuario = usuarioOptional.get();

            String nombreRol = "ROLE_" + usuario.getRol().getNombre().toUpperCase(); // Recuperamos el rol del usuario

            SimpleGrantedAuthority autoridad = new SimpleGrantedAuthority(nombreRol);

            UsernamePasswordAuthenticationToken authentication = // Creamos la autenticación
                    new UsernamePasswordAuthenticationToken(
                            correo, null, List.of(autoridad)
                    );

            SecurityContextHolder.getContext() // Registramos al usuario autenticado
                    .setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException e) {

            SecurityContextHolder.clearContext();
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado"
            );
            return;
        }//token
        filterChain.doFilter(request, response);
    }//doFilter
}//classJwtFilter
