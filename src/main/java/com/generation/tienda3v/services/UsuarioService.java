package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Rol;
import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.repositories.RolRepository;
import com.generation.tienda3v.repositories.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {

    private static final String SALT_SECRETO = "Tienda3V_Salt_2026!";
    private static final long EXPIRACION_TOKEN_MS = 30 * 60 * 1000L; // 30 minutos

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, TokenRecuperacion> tokensRecuperacion = new ConcurrentHashMap<>();

    private record TokenRecuperacion(String token, long expiraEn) {}

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static boolean verificarPasswordCompatibilidad(String input, String guardado) {
        if (input == null || guardado == null) return false;
        if (guardado.startsWith("sha256$")) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest((SALT_SECRETO + input).getBytes(StandardCharsets.UTF_8));
                String calculado = "sha256$" + HexFormat.of().formatHex(hash);
                if (calculado.equals(guardado)) return true;
            } catch (NoSuchAlgorithmException ignored) {
            }
        }
        String base64Input = Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
        return input.equals(guardado) || base64Input.equals(guardado);
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

    // Autenticar usuario (respaldo directo para /api/usuarios/login)
    public Usuario autenticar(String correo, String passwordInput) {
        if (correo == null || correo.isBlank() || passwordInput == null || passwordInput.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo y contraseña obligatorios");
        }
        Usuario usuario = obtenerPorCorreo(correo);
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta se encuentra inactiva");
        }
        String guardado = usuario.getPasswordHash();
        boolean coincide = false;
        if (guardado != null && (guardado.startsWith("$2a$") || guardado.startsWith("$2b$") || guardado.startsWith("$2y$"))) {
            coincide = passwordEncoder.matches(passwordInput, guardado);
        } else if (verificarPasswordCompatibilidad(passwordInput, guardado)) {
            coincide = true;
            usuario.setPasswordHash(passwordEncoder.encode(passwordInput));
            usuarioRepository.save(usuario);
        }

        if (!coincide) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
        return usuario;
    }

    public Map<String, String> generarTokenRecuperacion(String correo) {
        Usuario usuario = obtenerPorCorreo(correo);
        String correoNorm = usuario.getCorreo().toLowerCase();
        long expira = System.currentTimeMillis() + EXPIRACION_TOKEN_MS;
        String raw = correoNorm + ":" + expira + ":" + SALT_SECRETO;
        String token;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            token = HexFormat.of().formatHex(digest.digest(raw.getBytes(StandardCharsets.UTF_8))).substring(0, 32);
        } catch (NoSuchAlgorithmException e) {
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        }
        tokensRecuperacion.put(correoNorm, new TokenRecuperacion(token, expira));
        return Map.of(
                "correo", usuario.getCorreo(),
                "nombre", (usuario.getNombre() + " " + usuario.getApellido()).trim(),
                "token", token
        );
    }

    public Usuario actualizarPasswordPorCorreo(String correo, String nuevoPasswordInput, String tokenRecibido) {
        if (nuevoPasswordInput == null || nuevoPasswordInput.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña es obligatoria");
        }
        Usuario existente = obtenerPorCorreo(correo);
        String correoNorm = existente.getCorreo().toLowerCase();

        TokenRecuperacion guardado = tokensRecuperacion.get(correoNorm);
        if (guardado != null) {
            if (System.currentTimeMillis() > guardado.expiraEn()) {
                tokensRecuperacion.remove(correoNorm);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El enlace de recuperación ha expirado");
            }
            if (tokenRecibido == null || !guardado.token().equals(tokenRecibido.trim())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token de recuperación inválido");
            }
            tokensRecuperacion.remove(correoNorm);
        }

        existente.setPasswordHash(passwordEncoder.encode(nuevoPasswordInput));
        return usuarioRepository.save(existente);
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

        // Buscar automáticamente el rol cliente (o validar rol en entorno de pruebas)
        Optional<Rol> rolCliente = rolRepository.findByNombre("cliente");
        if (rolCliente.isPresent()) {
            // Asignar cliente sin importar el rol enviado en Postman
            usuario.setRol(rolCliente.get());
        } else {
            validarRol(usuario);
        }

        // Codificar la contraseña con BCrypt
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
