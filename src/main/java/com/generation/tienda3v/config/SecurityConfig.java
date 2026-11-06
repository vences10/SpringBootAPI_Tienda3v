package com.generation.tienda3v.config;

import com.generation.tienda3v.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//para modificar los puertos de el VSC y del IntelliJ
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
/*
 *Configurar qué rutas son públicas.
 * Exigir autenticación para las demás rutas.
 * Desactivar sesiones tradicionales.
 * Registrar nuestro filtro JWT.
  */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://127.0.0.1:5501",
                "http://localhost:5501",
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "*"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())// Desactivamos CSRF porque utilizamos JWT
                .sessionManagement(session -> // No utilizamos sesiones tradicionales
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable()) // Desactivamos los formularios y autenticación básica
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth // Configuramos los permisos
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers( // Rutas públicas de autenticación, registro y compras de cliente
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/usuarios",
                                "/api/usuarios/login",
                                "/api/usuarios/solicitar-recuperacion",
                                "/api/mensajes-contacto",
                                "/api/pedidos",
                                "/api/detalles-pedido"
                        ).permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/password").permitAll()
                        .requestMatchers( // Catálogo público para navegación en la tienda
                                HttpMethod.GET,
                                "/api/productos/**",
                                "/api/categorias/**",
                                "/api/sucursales/**",
                                "/api/cupones/**",
                                "/api/metodos-pago/**",
                                "/api/roles/**",
                                "/api/usuarios/correo/**"
                        ).permitAll()
                        // Solo administradores pueden crear, modificar o eliminar productos y cupones
                        .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/cupones/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/cupones/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/cupones/**").hasRole("ADMINISTRADOR")
                        // Clientes autenticados y administradores pueden consultar/actualizar su propio perfil y ver/modificar recolección de sus pedidos
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/*").hasAnyRole("ADMINISTRADOR", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*").hasAnyRole("ADMINISTRADOR", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/**", "/api/detalles-pedido/**").hasAnyRole("ADMINISTRADOR", "CLIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/recoleccion").hasAnyRole("ADMINISTRADOR", "CLIENTE")
                        // Solo administradores pueden listar todos los usuarios, eliminarlos o cambiar estado de pedidos
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/**").hasRole("ADMINISTRADOR")
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
