package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario_sucursal")
public class InventarioSucursal {

    // Representa la llave primaria compuesta: sucursal_id + producto_id
    @EmbeddedId
    private InventarioSucursalId id_sucursal;

    @MapsId("sucursalId") // Relaciona el inventario con una sucursal
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @MapsId("productoId") // Relaciona el inventario con un producto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "existencia", nullable = false) // Cantidad de unidades disponibles en la sucursal
    private Long existencia = 0L;

    @Column( // Fecha y hora de la última actualización.
            name = "actualizado_en", insertable = false, updatable = false)
    private LocalDateTime actualizadoEn;

    public InventarioSucursal() { // Constructor vacío requerido por JPA
    } //constructor

    public InventarioSucursalId getId() {
        return id_sucursal;
    }// getId

    public void setId(InventarioSucursalId id) {
        this.id_sucursal = id_sucursal;
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

    public Long getExistencia() {
        return existencia;
    }// getExistencia

    public void setExistencia(Long existencia) {
        this.existencia = existencia;
    }//setExistencia

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }// getActualizadoEn
}//class InventarioSucursal


