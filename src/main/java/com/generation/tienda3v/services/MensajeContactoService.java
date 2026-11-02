package com.generation.tienda3v.services;

import com.generation.tienda3v.models.MensajeContacto;
import com.generation.tienda3v.repositories.MensajeContactoRepository;
import com.generation.tienda3v.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MensajeContactoService {

    private final MensajeContactoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public MensajeContactoService(MensajeContactoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<MensajeContacto> obtenerTodos() {
        return repository.findAll();
    }

    public MensajeContacto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensaje de contacto no encontrado"));
    }

    public MensajeContacto crear(MensajeContacto mensaje) {
        if (mensaje.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        validarUsuario(mensaje.getUsuarioId());
        return repository.save(mensaje);
    }

    public MensajeContacto actualizar(Long id, MensajeContacto mensaje) {
        MensajeContacto existente = obtenerPorId(id);
        validarUsuario(mensaje.getUsuarioId());
        existente.setUsuarioId(mensaje.getUsuarioId());
        existente.setNombre(mensaje.getNombre());
        existente.setCorreo(mensaje.getCorreo());
        existente.setTelefono(mensaje.getTelefono());
        existente.setMotivo(mensaje.getMotivo());
        existente.setMensaje(mensaje.getMensaje());
        existente.setAceptaAvisoPrivacidad(mensaje.getAceptaAvisoPrivacidad());
        existente.setEstado(mensaje.getEstado());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        repository.delete(obtenerPorId(id));
    }

    private void validarUsuario(Long usuarioId) {
        if (usuarioId != null && !usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
}
