package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findBySlug(String slug);
    List<Categoria> findByActivaTrue();
    boolean existsByNombre(String nombre);
    boolean existsBySlug(String slug);
}// CategoriaRepository