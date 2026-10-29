package com.generation.tienda3v.models;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    // Constructor vacío
    public DetallePedido() {}

    // Constructor con parámetros
    public DetallePedido(Long pedidoId, Long productoId, String nombreProducto, Integer cantidad, Double precioUnitario, Double subtotal) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = (cantidad != null && cantidad < 0) ? 1 : cantidad;
        this.precioUnitario = (precioUnitario != null && precioUnitario < 0) ? 0.0 : precioUnitario;
        this.subtotal = (subtotal != null && subtotal < 0) ? 0.0 : subtotal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = (cantidad != null && cantidad < 0) ? 1 : cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = (precioUnitario != null && precioUnitario < 0) ? 0.0 : precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = (subtotal != null && subtotal < 0) ? 0.0 : subtotal;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", productoId=" + productoId +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
