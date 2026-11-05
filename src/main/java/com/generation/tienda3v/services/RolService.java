package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Rol;
import com.generation.tienda3v.repositories.RolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RolService {

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }

    public List<Rol> obtenerTodos() {
        return repository.findAll();
    }

    public Rol obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
    }

    public Rol crear(Rol rol) {
        if (rol.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        return repository.save(rol);
    }

    public Rol actualizar(Long id, Rol rol) {
        Rol existente = obtenerPorId(id);
        existente.setNombre(rol.getNombre());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Rol existente = obtenerPorId(id);
        repository.delete(existente);
    }
}
