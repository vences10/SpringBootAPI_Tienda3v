package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.repositories.RolRepository;
import com.generation.tienda3v.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {

    private static final String PREFIJO_HASH = "sha256$";
    private static final String SALT_TIENDA3V = "Tienda3V_Salt_2026!";

    private record TokenRecuperacion(String token, LocalDateTime expiraEn) {}

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final Map<String, TokenRecuperacion> tokensRecuperacion = new ConcurrentHashMap<>();

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario obtenerPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo es obligatorio");
        }
        return usuarioRepository.findByCorreo(correo.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public String generarTokenRecuperacion(String correo) {
        Usuario usuario = obtenerPorCorreo(correo);
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        tokensRecuperacion.put(usuario.getCorreo().toLowerCase(),
                new TokenRecuperacion(token, LocalDateTime.now().plusMinutes(30)));
        return token;
    }

    public Usuario autenticar(String correo, String passwordInput) {
        if (correo == null || correo.isBlank() || passwordInput == null || passwordInput.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo y contraseña obligatorios");
        }
        Usuario usuario = obtenerPorCorreo(correo);
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta se encuentra inactiva");
        }
        String guardado = usuario.getPasswordHash();
        String codificadoBase64 = Base64.getEncoder()
                .encodeToString(passwordInput.getBytes(StandardCharsets.UTF_8));
        String hashDirecto = hashearPassword(passwordInput);
        String hashDeBase64 = hashearPassword(codificadoBase64);

        boolean coincide = passwordInput.equals(guardado)
                || codificadoBase64.equals(guardado)
                || hashDirecto.equals(guardado)
                || hashDeBase64.equals(guardado);

        if (!coincide) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
        return usuario;
    }

    public Usuario actualizarPasswordPorCorreo(String correo, String nuevoPasswordHash, String token) {
        if (nuevoPasswordHash == null || nuevoPasswordHash.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña es obligatoria");
        }
        Usuario existente = obtenerPorCorreo(correo);
        String correoNorm = existente.getCorreo().toLowerCase();

        TokenRecuperacion tokenGuardado = tokensRecuperacion.get(correoNorm);
        if (tokenGuardado != null) {
            if (LocalDateTime.now().isAfter(tokenGuardado.expiraEn())) {
                tokensRecuperacion.remove(correoNorm);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El enlace de recuperación ha expirado");
            }
            if (token != null && !token.isBlank() && !tokenGuardado.token().equalsIgnoreCase(token.trim())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token de recuperación inválido");
            }
            tokensRecuperacion.remove(correoNorm);
        }

        existente.setPasswordHash(asegurarHash(nuevoPasswordHash));
        return usuarioRepository.save(existente);
    }

    public Usuario crear(Usuario usuario) {
        if (usuario.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        validarRol(usuario);
        if (usuario.getPasswordHash() != null) {
            usuario.setPasswordHash(asegurarHash(usuario.getPasswordHash()));
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        usuarioRepository.findByCorreo(usuario.getCorreo()).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
            }
        });
        validarRol(usuario);
        existente.setRol(usuario.getRol());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setCorreo(usuario.getCorreo());
        existente.setTelefono(usuario.getTelefono());
        existente.setAceptaTerminos(usuario.getAceptaTerminos());
        existente.setActivo(usuario.getActivo());
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().isBlank()) {
            existente.setPasswordHash(asegurarHash(usuario.getPasswordHash()));
        }
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(obtenerPorId(id));
    }

    private void validarRol(Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el rol del usuario");
        }
        usuario.setRol(rolRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado")));
    }

    private String asegurarHash(String valor) {
        if (valor == null || valor.startsWith(PREFIJO_HASH)) {
            return valor;
        }
        return hashearPassword(valor);
    }

    public static String hashearPassword(String entrada) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((SALT_TIENDA3V + entrada).getBytes(StandardCharsets.UTF_8));
            return PREFIJO_HASH + HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }
}
