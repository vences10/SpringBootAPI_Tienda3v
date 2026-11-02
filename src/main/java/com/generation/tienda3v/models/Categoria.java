package com.generation.tienda3v.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {
    }//Categoria

    public Long getIdCategoria() {
        return idCategoria;
    }//getIdCategoria

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }//setIdCategoria

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

}// class Categoria
