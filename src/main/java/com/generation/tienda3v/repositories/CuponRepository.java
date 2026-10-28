package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuponRepository extends JpaRepository<Cupon, Integer> {
    // aqui ya vienen todos los metodos del CRUD por defecto
}