package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Sucursal;
import com.generation.tienda3v.repositories.SucursalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> obtenerTodos() {
        return repository.findAll();
    }

    public Sucursal obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrado"));
    }

    public Sucursal crear(Sucursal sucursal) {
        if (sucursal.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        return repository.save(sucursal);
    }

    public Sucursal actualizar(Long id, Sucursal sucursal) {
        Sucursal existente = obtenerPorId(id);
        existente.setNombre(sucursal.getNombre());
        existente.setDireccion(sucursal.getDireccion());
        existente.setCiudad(sucursal.getCiudad());
        existente.setEstado(sucursal.getEstado());
        existente.setCodigoPostal(sucursal.getCodigoPostal());
        existente.setHorario(sucursal.getHorario());
        existente.setMapaUrl(sucursal.getMapaUrl());
        existente.setActiva(sucursal.getActiva());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Sucursal existente = obtenerPorId(id);
        repository.delete(existente);
    }
}
