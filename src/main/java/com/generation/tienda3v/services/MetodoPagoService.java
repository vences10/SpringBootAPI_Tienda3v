package com.generation.tienda3v.services;

import com.generation.tienda3v.models.MetodoPago;
import com.generation.tienda3v.repositories.MetodoPagoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository repository;

    public MetodoPagoService(MetodoPagoRepository repository) {
        this.repository = repository;
    }

    public List<MetodoPago> obtenerTodos() {
        return repository.findAll();
    }

    public MetodoPago obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
    }

    public MetodoPago crear(MetodoPago metodoPago) {
        if (metodoPago.getIdMetodoPago() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        return repository.save(metodoPago);
    }

    public MetodoPago actualizar(Long id, MetodoPago metodoPago) {
        MetodoPago existente = obtenerPorId(id);
        existente.setNombre(metodoPago.getNombre());
        existente.setActivo(metodoPago.getActivo());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        MetodoPago existente = obtenerPorId(id);
        repository.delete(existente);
    }
}
