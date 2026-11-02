package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario_sucursal")
public class InventarioSucursal {

    // Representa la llave primaria compuesta: id_sucursal + id_producto
    @EmbeddedId
    private InventarioSucursalId id_sucursal;

    @MapsId("sucursalId") // Relaciona el inventario con una sucursal
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    @MapsId("productoId") // Relaciona el inventario con un producto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "existencia", nullable = false) // Cantidad de unidades disponibles en la sucursal
    private Integer existencia = 0;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @PrePersist
    @PreUpdate
    protected void actualizarFecha() {
        this.actualizadoEn = LocalDateTime.now();
    }

    public InventarioSucursal() { // Constructor vacío requerido por JPA
    } //constructor

    public InventarioSucursalId getId() {
        return id_sucursal;
    }// getId

    public void setId(InventarioSucursalId id) {
        this.id_sucursal = id;
    } // setId

    public Sucursal getSucursal() {
        return sucursal;
    } // getSucursal

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    } // setSucursal

    public Producto getProducto() {
        return producto;
    } // getProducto

    public void setProducto(Producto producto) {
        this.producto = producto;
    } // setProducto

    public Integer getExistencia() {
        return existencia;
    }// getExistencia

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }//setExistencia

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }// getActualizadoEn
}//class InventarioSucursal


