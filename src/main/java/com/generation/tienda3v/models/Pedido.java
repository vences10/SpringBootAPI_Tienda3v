package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @Column(name = "metodo_pago_id", nullable = false)
    private Long metodoPagoId;

    @Column(name = "cupon_id")
    private Long cuponId;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "estado_pago", nullable = false, length = 30)
    private String estadoPago;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "correo_cliente", nullable = false, length = 254)
    private String correoCliente;

    @Column(name = "telefono_cliente", length = 20)
    private String telefonoCliente;

    @Column(name = "codigo_cupon", length = 30)
    private String codigoCupon;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    // Constructor vacío
    public Pedido() {}

    // Constructor con parámetros principales
    public Pedido(Long usuarioId, Long sucursalId, Long metodoPagoId, Long cuponId, String estado,
                  String estadoPago, String nombreCliente, String correoCliente, String telefonoCliente,
                  String codigoCupon, Double subtotal, Double descuento, Double total) {
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
        this.subtotal = (subtotal != null && subtotal < 0) ? 0.0 : subtotal;
        this.descuento = (descuento != null && descuento < 0) ? 0.0 : descuento;
        this.total = (total != null && total < 0) ? 0.0 : total;
        this.creadoEn = LocalDateTime.now();
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
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
