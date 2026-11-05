package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.models.Rol;
import com.generation.tienda3v.repositories.RolRepository;
import com.generation.tienda3v.repositories.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    // Obtener usuario por correo
    public Usuario obtenerPorCorreo(String correo) {

        if (correo == null || correo.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El correo es obligatorio");
        }

        return usuarioRepository.findByCorreo(
                correo.trim().toLowerCase()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    // Registrar un usuario nuevo
    public Usuario crear(Usuario usuario) {

        // Validar que el ID no venga en el registro
        if (usuario.getId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El id se genera automáticamente");
        }

        // Validar que el usuario exista y tenga datos obligatorios
        if (usuario.getCorreo() == null ||
                usuario.getCorreo().isBlank() ||
                usuario.getPasswordHash() == null ||
                usuario.getPasswordHash().isBlank() ||
                usuario.getNombre() == null ||
                usuario.getNombre().isBlank() ||
                usuario.getApellido() == null ||
                usuario.getApellido().isBlank() ||
                usuario.getTelefono() == null ||
                usuario.getTelefono().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Todos los campos obligatorios deben estar completos");
        }

        // Normalizar correo
        String correo = usuario.getCorreo().trim().toLowerCase();
        usuario.setCorreo(correo);

        // Verificar que el correo no esté registrado
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        // Buscar automáticamente el rol cliente
        Rol rolCliente = rolRepository.findByNombre("cliente")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "No se encontró el rol cliente"));

        // Asignar cliente sin importar el rol enviado en Postman
        usuario.setRol(rolCliente);

// Valores por defecto
        usuario.setActivo(true);

        if (usuario.getAceptaTerminos() == null) {
            usuario.setAceptaTerminos(false);
        }

// Codificar contraseña
        usuario.setPasswordHash(
                passwordEncoder.encode(usuario.getPasswordHash())
        );

// Guardar usuario
        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    public Usuario actualizar(Long id, Usuario usuario) {

        Usuario existente = obtenerPorId(id);

        if (usuario.getCorreo() == null ||
                usuario.getCorreo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El correo es obligatorio");
        }

        String correo = usuario.getCorreo().trim().toLowerCase();

        // Verificar que el correo no pertenezca a otro usuario
        usuarioRepository.findByCorreo(correo).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "El correo ya está registrado");
            }
        });

        // Validar el rol recibido
        validarRol(usuario);

        existente.setRol(usuario.getRol());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setCorreo(correo);
        existente.setTelefono(usuario.getTelefono());
        existente.setAceptaTerminos(usuario.getAceptaTerminos());
        existente.setActivo(usuario.getActivo());

        // Actualizar contraseña solamente si se envía una nueva
        if (usuario.getPasswordHash() != null &&
                !usuario.getPasswordHash().isBlank()) {

            existente.setPasswordHash(
                    passwordEncoder.encode(usuario.getPasswordHash())
            );
        }

        return usuarioRepository.save(existente);
    }

    // Eliminar usuario
    public void eliminar(Long id) {
        usuarioRepository.delete(obtenerPorId(id));
    }

    // Validar que el rol exista en la base de datos
    private void validarRol(Usuario usuario) {

        if (usuario.getRol() == null ||
                usuario.getRol().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Indica el rol del usuario");
        }

        usuario.setRol(
                rolRepository.findById(usuario.getRol().getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Rol no encontrado"))
        );
    }
}//class UsuarioService
