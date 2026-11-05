package com.generation.tienda3v.services;

import com.generation.tienda3v.models.DetallePedido;
import com.generation.tienda3v.repositories.DetallePedidoRepository;
import com.generation.tienda3v.repositories.PedidoRepository;
import com.generation.tienda3v.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, PedidoRepository pedidoRepository,
                               ProductoRepository productoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    public List<DetallePedido> obtenerTodos() {
        return detallePedidoRepository.findAll();
    }

    public List<DetallePedido> obtenerPorPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    public DetallePedido obtenerPorId(Long id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));
    }

    public DetallePedido crear(DetallePedido detalle) {
        if (detalle.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del detalle se genera automáticamente");
        }
        validarReferencias(detalle);
        return detallePedidoRepository.save(detalle);
    }

    public DetallePedido actualizar(Long id, DetallePedido detalle) {
        DetallePedido existente = obtenerPorId(id);
        validarReferencias(detalle);
        existente.setPedidoId(detalle.getPedidoId());
        existente.setProductoId(detalle.getProductoId());
        existente.setNombreProducto(detalle.getNombreProducto());
        existente.setCantidad(detalle.getCantidad());
        existente.setPrecioUnitario(detalle.getPrecioUnitario());
        existente.setSubtotal(detalle.getSubtotal());
        return detallePedidoRepository.save(existente);
    }

    public void eliminar(Long id) {
        detallePedidoRepository.delete(obtenerPorId(id));
    }

    private void validarReferencias(DetallePedido detalle) {
        if (detalle.getPedidoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido es obligatorio");
        }
        if (!pedidoRepository.existsById(detalle.getPedidoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }
        if (detalle.getProductoId() != null && !productoRepository.existsById(detalle.getProductoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
    }
}
