package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Usuario;
import com.generation.tienda3v.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/correo/{correo}")
    public Usuario obtenerPorCorreo(@PathVariable String correo) {
        return usuarioService.obtenerPorCorreo(correo);
    }

    @PostMapping("/solicitar-recuperacion")
    public Map<String, String> solicitarRecuperacion(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        String token = usuarioService.generarTokenRecuperacion(correo);
        return Map.of(
                "correo", usuario.getCorreo(),
                "nombre", (usuario.getNombre() + " " + usuario.getApellido()).trim(),
                "token", token
        );
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String password = credenciales.getOrDefault("passwordHash", credenciales.get("password"));
        return usuarioService.autenticar(correo, password);
    }

    @PutMapping("/password")
    public Usuario actualizarPassword(@RequestBody Map<String, String> datos) {
        String correo = datos.get("correo");
        String passwordHash = datos.getOrDefault("passwordHash", datos.get("password"));
        String token = datos.get("token");
        return usuarioService.actualizarPasswordPorCorreo(correo, passwordHash, token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crear(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
