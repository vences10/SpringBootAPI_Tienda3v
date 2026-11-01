package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_categoria_fk",
            referencedColumnName = "id_categoria",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_productos_categoria")
    )
    private Categoria categoria;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "descripcion_producto", columnDefinition = "TEXT")
    private String descripcionProducto;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "sku", nullable = false, length = 40, unique = true)
    private String sku;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }//onCreate

    public Producto() {
    }//Producto

    public Long getIdProducto() {
        return idProducto;
    }//getIdProducto

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }//setIdProducto

    public Categoria getCategoria() {
        return categoria;
    }//getCategoria

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }//setCategoria

    public String getNombreProducto() {
        return nombreProducto;
    }//getNombreProducto

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }//setNombreProducto

    public String getDescripcionProducto() {
        return descripcionProducto;
    }//getDescripcionProducto

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }//setDescripcionProducto

    public String getMarca() {
        return marca;
    }//getMarca

    public void setMarca(String marca) {
        this.marca = marca;
    }//setMarca

    public String getSku() {
        return sku;
    }//getSku

    public void setSku(String sku) {
        this.sku = sku;
    }//setSku

    public BigDecimal getPrecio() {
        return precio;
    }//getPrecio

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }//setPrecio

    public Integer getStock() {
        return stock;
    }//getStock

    public void setStock(Integer stock) {
        this.stock = stock;
    }//setStock

    public Boolean getActivo() {
        return activo;
    }//getActivo

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }//setActivo

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }//getFechaCreacion

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }//setFechaCreacion

}// class Producto