package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Cupon;
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

import java.math.BigDecimal;
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

    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByCreadoEnDesc(usuarioId);
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
        validarImportes(pedido);

        if (pedido.getCuponId() != null) {
            Cupon cupon = cuponRepository.findById(pedido.getCuponId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
            if (Boolean.FALSE.equals(cupon.getActivo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cupón no se encuentra activo");
            }
            int usosActuales = cupon.getUsos() == null ? 0 : cupon.getUsos();
            if (cupon.getMaximoUsos() != null && usosActuales >= cupon.getMaximoUsos()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cupón ha alcanzado su límite de usos");
            }
            cupon.setUsos(usosActuales + 1);
            cuponRepository.save(cupon);
        }

        LocalDateTime ahora = LocalDateTime.now();
        pedido.setCreadoEn(ahora);
        pedido.setActualizadoEn(ahora);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Long id, Pedido pedido) {
        Pedido existente = obtenerPorId(id);
        validarReferencias(pedido);
        validarImportes(pedido);
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

    public Pedido actualizarEstado(Long id, String estado, String estadoPago) {
        Pedido existente = obtenerPorId(id);
        if (estado != null && !estado.isBlank()) {
            existente.setEstado(estado.trim());
        }
        if (estadoPago != null && !estadoPago.isBlank()) {
            existente.setEstadoPago(estadoPago.trim());
        }
        existente.setActualizadoEn(LocalDateTime.now());
        return pedidoRepository.save(existente);
    }

    public void eliminar(Long id) {
        pedidoRepository.delete(obtenerPorId(id));
    }

    private void validarImportes(Pedido pedido) {
        BigDecimal subtotal = pedido.getSubtotal() == null ? BigDecimal.ZERO : pedido.getSubtotal();
        BigDecimal descuento = pedido.getDescuento() == null ? BigDecimal.ZERO : pedido.getDescuento();
        BigDecimal total = pedido.getTotal() == null ? BigDecimal.ZERO : pedido.getTotal();

        if (subtotal.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(BigDecimal.ZERO) < 0 || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los importes del pedido no pueden ser negativos");
        }
        if (subtotal.subtract(descuento).compareTo(total) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total debe ser igual a subtotal menos descuento");
        }
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
