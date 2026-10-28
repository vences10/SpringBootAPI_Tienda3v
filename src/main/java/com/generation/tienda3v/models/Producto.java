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
    private Integer idProducto;

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

    @Column(name = "precio_unitario", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Producto() {
    } //producto

    public Integer getIdProducto() {
        return idProducto;
    } //getIdProducto

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    } //setIdProducto

    public Categoria getCategoria() {
        return categoria;
    } //getCategoria

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }//setCategoria

    public String getNombreProducto() {
        return nombreProducto;
    }//getNombreProducto

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    } //setNombreProducto

    public String getDescripcionProducto() {
        return descripcionProducto;
    } //getDescripcionProducto

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    } //setDescripcionProducto

    public String getMarca() {
        return marca;
    } //getMarca

    public void setMarca(String marca) {
        this.marca = marca;
    } //setMarca

    public String getSku() {
        return sku;
    } //setSku

    public void setSku(String sku) {
        this.sku = sku;
    } //getSku

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    } //getPrecioUnitario

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    } //setPrecioUnitario

    public String getImagenUrl() {
        return imagenUrl;
    } // setImagenUrl

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    } //setImagenUrl

    public Boolean getActivo() {
        return activo;
    } //getActivo

    public void setActivo(Boolean activo) {
        this.activo = activo;
    } //setActivo

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    } //getFechaCreación

} //class Producto