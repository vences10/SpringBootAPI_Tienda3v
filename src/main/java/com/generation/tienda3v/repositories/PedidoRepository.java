package com.generation.tienda3v.repositories;

import com.generation.tienda3v.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}