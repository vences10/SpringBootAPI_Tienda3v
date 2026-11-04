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
