package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.InventarioSucursal;
import com.generation.tienda3v.models.InventarioSucursalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioSucursalRepository
        extends JpaRepository<InventarioSucursal, InventarioSucursalId> {
}

