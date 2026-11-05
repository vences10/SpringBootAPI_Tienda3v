package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminAuthService {

    private final UsuarioRepository usuarioRepository;

    public AdminAuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica que el usuario sea administrador cuando la petición proviene del Frontend
     * (encabezados X-Usuario-Id o X-Usuario-Rol).
     */
    public void verificarAdmin(HttpServletRequest request) {
        if (request == null) return;

        String rolHeader = request.getHeader("X-Usuario-Rol");
        String idHeader = request.getHeader("X-Usuario-Id");

        if (rolHeader == null && idHeader == null) {
            return;
        }

        if (rolHeader != null) {
            String rolNorm = rolHeader.trim().toLowerCase();
            if (!rolNorm.equals("admin") && !rolNorm.equals("administrador")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Acceso denegado: se requiere rol de administrador");
            }
        }

        if (idHeader != null && !idHeader.isBlank()) {
            try {
                Long idUsuario = Long.parseLong(idHeader.trim());
                Usuario usuario = usuarioRepository.findById(idUsuario)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "Usuario de sesión no válido"));
                if (Boolean.FALSE.equals(usuario.getActivo())
                        || usuario.getRol() == null
                        || !"administrador".equalsIgnoreCase(usuario.getRol().getNombre())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "Acceso denegado: tu cuenta no tiene permisos de administrador");
                }
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Identificador de usuario inválido");
            }
        }
    }
}

