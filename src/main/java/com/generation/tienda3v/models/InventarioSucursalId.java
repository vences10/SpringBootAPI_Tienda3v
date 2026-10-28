package com.generation.tienda3v.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InventarioSucursalId implements Serializable {

    @Column(name = "sucursal_id")
    private Long sucursalId; // ID de la sucursal
    @Column(name = "producto_id")
    private Long productoId; // ID del producto

    public InventarioSucursalId() {
    } //contructor

    public InventarioSucursalId(Long sucursalId, Long productoId) {
        this.sucursalId = sucursalId;
        this.productoId = productoId;
    } // metodo InventarioSucursal

    public Long getSucursalId() {
        return sucursalId;
    } //getSucursalId

    public void setSucursalId(Long sucursalId) {
        this.sucursalId = sucursalId;
    } //setSucursalId

    public Long getProductoId() {
        return productoId;
    } //getProductoId

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }//getProductoId

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventarioSucursalId)) return false;
        InventarioSucursalId that = (InventarioSucursalId) o;
        return Objects.equals(sucursalId, that.sucursalId)
                && Objects.equals(productoId, that.productoId);
    } //override

    @Override
    public int hashCode() {
        return Objects.hash(sucursalId, productoId);
    } //hashCode
} //class InventarioSucursalId
