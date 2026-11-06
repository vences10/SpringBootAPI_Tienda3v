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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final CuponRepository cuponRepository;

    private final Map<Long, DatosRecoleccion> recoleccionesPorPedido = new ConcurrentHashMap<>();

    private record DatosRecoleccion(String fechaRecoleccion, String horarioRecoleccion) {}

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

    private Pedido enriquecerRecoleccion(Pedido pedido) {
        if (pedido != null && pedido.getId() != null) {
            DatosRecoleccion datos = recoleccionesPorPedido.get(pedido.getId());
            if (datos != null) {
                pedido.setFechaRecoleccion(datos.fechaRecoleccion());
                pedido.setHorarioRecoleccion(datos.horarioRecoleccion());
            }
        }
        return pedido;
    }

    private void guardarRecoleccionEnMemoria(Pedido pedido, String fecha, String horario) {
        if (pedido != null && pedido.getId() != null && (fecha != null || horario != null)) {
            recoleccionesPorPedido.put(pedido.getId(), new DatosRecoleccion(fecha, horario));
            pedido.setFechaRecoleccion(fecha);
            pedido.setHorarioRecoleccion(horario);
        }
    }

    public List<Pedido> obtenerTodos() {
        List<Pedido> lista = pedidoRepository.findAll();
        lista.forEach(this::enriquecerRecoleccion);
        return lista;
    }

    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        List<Pedido> lista = pedidoRepository.findByUsuarioIdOrderByCreadoEnDesc(usuarioId);
        lista.forEach(this::enriquecerRecoleccion);
        return lista;
    }

    public Pedido obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        return enriquecerRecoleccion(pedido);
    }

    public Pedido crear(Pedido pedido) {
        if (pedido.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del pedido se genera automáticamente");
        }
        validarReferencias(pedido);
        validarImportes(pedido);

        String fechaRecoleccion = pedido.getFechaRecoleccion();
        String horarioRecoleccion = pedido.getHorarioRecoleccion();

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
        Pedido guardado = pedidoRepository.save(pedido);
        guardarRecoleccionEnMemoria(guardado, fechaRecoleccion, horarioRecoleccion);
        return guardado;
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
        Pedido guardado = pedidoRepository.save(existente);
        if (pedido.getFechaRecoleccion() != null || pedido.getHorarioRecoleccion() != null) {
            guardarRecoleccionEnMemoria(guardado, pedido.getFechaRecoleccion(), pedido.getHorarioRecoleccion());
        } else {
            enriquecerRecoleccion(guardado);
        }
        return guardado;
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
        return enriquecerRecoleccion(pedidoRepository.save(existente));
    }

    public Pedido actualizarRecoleccion(Long id, String fechaRecoleccion, String horarioRecoleccion) {
        if (fechaRecoleccion == null || fechaRecoleccion.isBlank() || horarioRecoleccion == null || horarioRecoleccion.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha y el horario de recolección son obligatorios");
        }
        Pedido existente = obtenerPorId(id);
        String estadoActual = existente.getEstado() == null ? "pendiente" : existente.getEstado().trim().toLowerCase();
        if (Set.of("entregado", "completado", "cancelado").contains(estadoActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar un pedido entregado o cancelado");
        }
        existente.setActualizadoEn(LocalDateTime.now());
        Pedido guardado = pedidoRepository.save(existente);
        guardarRecoleccionEnMemoria(guardado, fechaRecoleccion.trim(), horarioRecoleccion.trim());
        return guardado;
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
