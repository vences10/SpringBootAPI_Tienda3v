package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findBySlug(String slug);

    List<Categoria> findByActivaTrue();

    boolean existsByNombre(String nombre);

    boolean existsBySlug(String slug);
} // CategoriaRepository
