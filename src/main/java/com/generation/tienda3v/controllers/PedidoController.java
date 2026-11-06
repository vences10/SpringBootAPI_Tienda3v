package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Pedido;
import com.generation.tienda3v.services.AdminAuthService;
import com.generation.tienda3v.services.PedidoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;
    private final AdminAuthService adminAuthService;

    @Autowired
    public PedidoController(PedidoService pedidoService, AdminAuthService adminAuthService) {
        this.pedidoService = pedidoService;
        this.adminAuthService = adminAuthService;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.obtenerTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPorUsuario(@PathVariable Long usuarioId) {
        return pedidoService.obtenerPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return pedidoService.crear(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return pedidoService.actualizar(id, pedido);
    }

    @PatchMapping("/{id}/estado")
    public Pedido actualizarEstadoPedido(@PathVariable Long id, @RequestBody Map<String, String> datos, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return pedidoService.actualizarEstado(id, datos.get("estado"), datos.get("estadoPago"));
    }

    @PatchMapping("/{id}/recoleccion")
    public Pedido actualizarRecoleccionPedido(@PathVariable Long id, @RequestBody Map<String, String> datos) {
        return pedidoService.actualizarRecoleccion(id, datos.get("fechaRecoleccion"), datos.get("horarioRecoleccion"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPedido(@PathVariable Long id, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        pedidoService.eliminar(id);
    }
}
