package com.generation.tienda3v.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    @Bean //registra el codificador para poder inyectarlo en otros servicios.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //genera hashes seguros de las contraseñas y permite verificarlos.
    }
}
