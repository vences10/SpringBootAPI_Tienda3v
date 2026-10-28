package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    // aqui ya vienen todos los metodos del CRUD por defecto
}