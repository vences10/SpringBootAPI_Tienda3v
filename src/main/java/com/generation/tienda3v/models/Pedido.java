package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false, unique = true)
    private Long id;

    @Column(name = "id_usuario")
    private Long usuarioId;

    @Column(name = "id_sucursal", nullable = false)
    private Long sucursalId;

    @Column(name = "id_metodo_pago", nullable = false)
    private Long metodoPagoId;

    @Column(name = "id_cupon")
    private Long cuponId;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "pendiente";

    @Column(name = "estado_pago", nullable = false, length = 30)
    private String estadoPago = "pendiente";

    @Column(name = "nombre_cliente", nullable = false, length = 180)
    private String nombreCliente;

    @Column(name = "correo_cliente", nullable = false, length = 254)
    private String correoCliente;

    @Column(name = "telefono_cliente", nullable = false, length = 20)
    private String telefonoCliente;

    @Column(name = "codigo_cupon", length = 30)
    private String codigoCupon;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @Transient
    private String fechaRecoleccion;

    @Transient
    private String horarioRecoleccion;

    // Constructor vacío
    public Pedido() {}

    // Constructor con parámetros principales
    public Pedido(Long usuarioId, Long sucursalId, Long metodoPagoId, Long cuponId, String estado,
                  String estadoPago, String nombreCliente, String correoCliente, String telefonoCliente,
                  String codigoCupon, BigDecimal subtotal, BigDecimal descuento, BigDecimal total) {
        this.usuarioId = usuarioId;
        this.sucursalId = sucursalId;
        this.metodoPagoId = metodoPagoId;
        this.cuponId = cuponId;
        this.estado = estado;
        this.estadoPago = estadoPago;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.telefonoCliente = telefonoCliente;
        this.codigoCupon = codigoCupon;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creadoEn = ahora;
        this.actualizadoEn = ahora;
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Long sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Long getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(Long metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public Long getCuponId() {
        return cuponId;
    }

    public void setCuponId(Long cuponId) {
        this.cuponId = cuponId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCodigoCupon() {
        return codigoCupon;
    }

    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(LocalDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    public String getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(String fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public String getHorarioRecoleccion() {
        return horarioRecoleccion;
    }

    public void setHorarioRecoleccion(String horarioRecoleccion) {
        this.horarioRecoleccion = horarioRecoleccion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", sucursalId=" + sucursalId +
                ", metodoPagoId=" + metodoPagoId +
                ", cuponId=" + cuponId +
                ", estado='" + estado + '\'' +
                ", estadoPago='" + estadoPago + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", correoCliente='" + correoCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", codigoCupon='" + codigoCupon + '\'' +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                ", total=" + total +
                ", creadoEn=" + creadoEn +
                ", actualizadoEn=" + actualizadoEn +
                '}';
    }
}
