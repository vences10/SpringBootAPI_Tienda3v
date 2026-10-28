package com.generation.tienda3v.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sucursales", uniqueConstraints = {
                @UniqueConstraint( name = "uq_sucursales_nombre_direccion",
                        columnNames = {"nombre", "direccion"})
                })

public class Sucursal {

    @Id // Identificador único, generado automáticamente por MySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal", nullable = false, unique = true)
    private Long id_sucursal;
    // Nombre de la sucursal
    @Column(nullable = false, length = 120)
    private String nombre_sucursal;
    // Dirección física
    @Column(nullable = false, length = 255)
    private String direccion;
    // Ciudad donde se encuentra
    @Column(nullable = false, length = 100)
    private String ciudad;
    // Estado de la República
    @Column(nullable = false, length = 100)
    private String estado;
    // Código postal: puede quedar vacío
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;
    // Horario de atención: puede quedar vacío
    @Column(length = 150)
    private String horario;
    // Enlace al mapa: puede quedar vacío
    @Column(name = "mapa_url", length = 500)
    private String mapaUrl;
    // Indica si la sucursal está activa
    @Column(nullable = false)
    private Boolean activa = true;

    public Sucursal() {
    }//constructor JPA

    public Long getId() {
        return id_sucursal;
    }//getId

    public void setId(Long id) {
        this.id_sucursal = id_sucursal;
    }//setId

    public String getNombre() {
        return nombre_sucursal;
    }//getNombre

    public void setNombre(String nombre) {
        this.nombre_sucursal = nombre_sucursal;
    }//setNombre

    public String getDireccion() {
        return direccion;
    }//getDireccion

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }//setDireccion

    public String getCiudad() {
        return ciudad;
    }//getCiudad

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }//setCiudad

    public String getEstado() {
        return estado;
    }//getEstado

    public void setEstado(String estado) {
        this.estado = estado;
    }//setEstado

    public String getCodigoPostal() {
        return codigoPostal;
    }//getCodigoPostal

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }//setCodigoPostal

    public String getHorario() {
        return horario;
    }//getHorario

    public void setHorario(String horario) {
        this.horario = horario;
    }//setHorario

    public String getMapaUrl() {
        return mapaUrl;
    }//getMapaURL

    public void setMapaUrl(String mapaUrl) {
        this.mapaUrl = mapaUrl;
    }//setMapaURL

    public Boolean getActiva() {
        return activa;
    }//getActiva

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }//setActiva
}//class Sucursales

