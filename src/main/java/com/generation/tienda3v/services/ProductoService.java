package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Producto;
import com.generation.tienda3v.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }//obtenerTodos

    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrue();
    }//obtenerActivos

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }//obtenerPorId

    public Optional<Producto> obtenerPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }//obtenerPorSku

    public List<Producto> obtenerPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }//obtenerPorCategoria

    public List<Producto> obtenerPorCategoriaActivos(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }//obtenerPorCategoriaActivos

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }//guardar

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }//eliminar

}// class ProductoService