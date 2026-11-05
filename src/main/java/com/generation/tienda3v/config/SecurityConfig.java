package com.generation.tienda3v.config;

import com.generation.tienda3v.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/*
 *Configurar qué rutas son públicas.
 * Exigir autenticación para las demás rutas.
 * Desactivar sesiones tradicionales.
 * Registrar nuestro filtro JWT.
  */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean // Registramos nuestro filtro JWT
    public JwtFilter jwtFilter(UsuarioRepository usuarioRepository) {
        return new JwtFilter(usuarioRepository);
    }//jwtFilter

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(
            JwtFilter jwtFilter) {

        FilterRegistrationBean<JwtFilter> registration =
                new FilterRegistrationBean<>(jwtFilter);
        registration.setEnabled(false);
        return registration;
    }// Evitamos que Spring Boot registre el filtro dos veces

    @Bean // Configuramos las reglas de seguridad
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtFilter jwtFilter
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// Desactivamos CSRF porque utilizamos JWT
                .sessionManagement(session -> // No utilizamos sesiones tradicionales
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable()) // Desactivamos los formularios y autenticación básica
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth // Configuramos los permisos
                        .requestMatchers( // Rutas públicas
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/usuarios"
                        ).permitAll()
                        .requestMatchers("/api/usuarios/**") // Solo administradores pueden administrar usuarios
                        .hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated() // Las demás rutas requieren autenticación
                ).exceptionHandling(ex -> ex // Cuando no hay autenticación
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED, "Se requiere autenticación"
                            );
                        })
                        .accessDeniedHandler((request, response, exception) -> { // Cuando el usuario no tiene permisos
                            response.sendError(
                                    HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para acceder"
                            );
                        }))
                .addFilterBefore(
                        jwtFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }//securityFilter
}//class SecurityConfig