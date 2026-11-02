package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Producto;
import com.generation.tienda3v.repositories.ProductoRepository;
import com.generation.tienda3v.repositories.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }//obtenerTodos

    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrue();
    }//obtenerActivos

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }//obtenerPorId

    public List<Producto> obtenerPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }//obtenerPorCategoria

    public List<Producto> obtenerPorCategoriaActivos(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }//obtenerPorCategoriaActivos

    public Producto guardar(Producto producto) {
        if (producto.getIdProducto() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        validarCategoria(producto);
        return productoRepository.save(producto);
    }//guardar

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtenerPorId(id);
        validarCategoria(producto);
        existente.setCategoria(producto.getCategoria());
        existente.setNombreProducto(producto.getNombreProducto());
        existente.setDescripcionProducto(producto.getDescripcionProducto());
        existente.setPrecio(producto.getPrecio());
        existente.setPrecioAnterior(producto.getPrecioAnterior());
        existente.setImagenUrl(producto.getImagenUrl());
        existente.setActivo(producto.getActivo());
        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        productoRepository.delete(obtenerPorId(id));
    }//eliminar

    private void validarCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica la categoría del producto");
        }
        producto.setCategoria(categoriaRepository.findById(producto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada")));
    }

}// class ProductoService
