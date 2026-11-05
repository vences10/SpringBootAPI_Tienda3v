package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Pedido;
import com.generation.tienda3v.repositories.CuponRepository;
import com.generation.tienda3v.repositories.MetodoPagoRepository;
import com.generation.tienda3v.repositories.PedidoRepository;
import com.generation.tienda3v.repositories.SucursalRepository;
import com.generation.tienda3v.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final CuponRepository cuponRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository,
                         SucursalRepository sucursalRepository, MetodoPagoRepository metodoPagoRepository,
                         CuponRepository cuponRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.cuponRepository = cuponRepository;
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    public Pedido crear(Pedido pedido) {
        if (pedido.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del pedido se genera automáticamente");
        }
        validarReferencias(pedido);
        LocalDateTime ahora = LocalDateTime.now();
        pedido.setCreadoEn(ahora);
        pedido.setActualizadoEn(ahora);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Long id, Pedido pedido) {
        Pedido existente = obtenerPorId(id);
        validarReferencias(pedido);
        existente.setUsuarioId(pedido.getUsuarioId());
        existente.setSucursalId(pedido.getSucursalId());
        existente.setMetodoPagoId(pedido.getMetodoPagoId());
        existente.setCuponId(pedido.getCuponId());
        existente.setEstado(pedido.getEstado());
        existente.setEstadoPago(pedido.getEstadoPago());
        existente.setNombreCliente(pedido.getNombreCliente());
        existente.setCorreoCliente(pedido.getCorreoCliente());
        existente.setTelefonoCliente(pedido.getTelefonoCliente());
        existente.setCodigoCupon(pedido.getCodigoCupon());
        existente.setSubtotal(pedido.getSubtotal());
        existente.setDescuento(pedido.getDescuento());
        existente.setTotal(pedido.getTotal());
        existente.setActualizadoEn(LocalDateTime.now());
        return pedidoRepository.save(existente);
    }

    public void eliminar(Long id) {
        pedidoRepository.delete(obtenerPorId(id));
    }

    private void validarReferencias(Pedido pedido) {
        if (pedido.getSucursalId() == null || pedido.getMetodoPagoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal y el método de pago son obligatorios");
        }
        if (pedido.getUsuarioId() != null && !usuarioRepository.existsById(pedido.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        if (!sucursalRepository.existsById(pedido.getSucursalId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada");
        }
        if (!metodoPagoRepository.existsById(pedido.getMetodoPagoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado");
        }
        if (pedido.getCuponId() != null && !cuponRepository.existsById(pedido.getCuponId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado");
        }
    }
}
