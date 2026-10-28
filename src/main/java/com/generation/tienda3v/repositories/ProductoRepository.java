package com.generation.tienda3v.repository;

import com.generation.tienda3v.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    Optional<Producto> findBySku(String sku);

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);

    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(
            Integer idCategoria
    );

    List<Producto> findByNombreProductoContainingIgnoreCase(
            String nombre
    );

    boolean existsBySku(String sku);
}
