package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySku(String sku);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoria_IdCategoria(Long idCategoria);
    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(Long idCategoria);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);
    boolean existsBySku(String sku);
}// ProductoRepository