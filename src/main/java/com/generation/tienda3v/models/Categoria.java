package com.generation.tienda3v.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {
    } //categoria

    public Integer getIdCategoria() {
        return idCategoria;
    } //getIdCategoria

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    } //setIdCategoria

    public String getNombre() {
        return nombre;
    }//getNombre

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }//setNombre

    public String getSlug() {
        return slug;
    }//getSlug

    public void setSlug(String slug) {
        this.slug = slug;
    }//setSlug

    public Boolean getActiva() {
        return activa;
    }//getActiva

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }//setActiva

    public List<Producto> getProductos() {
        return productos;
    }//getProductos

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }//setProductos

}// class Categoría
