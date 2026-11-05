package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Cupon;
import com.generation.tienda3v.repositories.CuponRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CuponService {

    private final CuponRepository repository;

    public CuponService(CuponRepository repository) {
        this.repository = repository;
    }

    public List<Cupon> obtenerTodos() {
        return repository.findAll();
    }

    public Cupon obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
    }

    public Cupon crear(Cupon cupon) {
        if (cupon.getIdCupon() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        return repository.save(cupon);
    }

    public Cupon actualizar(Long id, Cupon cupon) {
        Cupon existente = obtenerPorId(id);
        existente.setCodigo(cupon.getCodigo());
        existente.setPorcentajeDescuento(cupon.getPorcentajeDescuento());
        existente.setFechaInicio(cupon.getFechaInicio());
        existente.setFechaFin(cupon.getFechaFin());
        existente.setMaximoUsos(cupon.getMaximoUsos());
        existente.setUsos(cupon.getUsos());
        existente.setActivo(cupon.getActivo());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Cupon existente = obtenerPorId(id);
        repository.delete(existente);
    }
}
