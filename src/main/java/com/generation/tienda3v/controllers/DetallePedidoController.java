package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.DetallePedido;
import com.generation.tienda3v.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> listarDetalles() {
        return detallePedidoService.obtenerTodos();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> listarPorPedido(@PathVariable Long pedidoId) {
        return detallePedidoService.obtenerPorPedido(pedidoId);
    }

    @GetMapping("/{id}")
    public DetallePedido obtenerPorId(@PathVariable Long id) {
        return detallePedidoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetallePedido crearDetalle(@RequestBody DetallePedido detalle) {
        return detallePedidoService.crear(detalle);
    }

    @PutMapping("/{id}")
    public DetallePedido actualizarDetalle(@PathVariable Long id, @RequestBody DetallePedido detalle) {
        return detallePedidoService.actualizar(id, detalle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarDetalle(@PathVariable Long id) {
        detallePedidoService.eliminar(id);
    }
}
