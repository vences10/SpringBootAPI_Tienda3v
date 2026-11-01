package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Producto;
import com.generation.tienda3v.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class ProductosService {

        @Autowired
        private ProductoRepository productosRepository;

        // Obtener todos los productos (GET)
        public List<Producto> obtenerProductos() {
            return productosRepository.findAll();
        } //obtenerProductos

        // Obtener un producto por ID (GET)
        public Optional<Producto> obtenerProductoPorId(Integer id) {
            return productosRepository.findById(id);
        } //obtenerProductoPorId

        // Obtener productos por ID de categoría (GET)
        public List<Producto> obtenerPorCategoria(Integer categoriaId) {
            return productosRepository.findByCategoria_IdCategoria(categoriaId);
        } //obtenerPorCategoria

        // Guardar o actualizar un producto (POST / PUT)
        public Producto guardarProducto(Producto producto) {
            return productosRepository.save(producto);
        } //guardarProducto

        // Eliminar un producto (DELETE)
        public void eliminarProducto(Integer id) {
            productosRepository.deleteById(id);
        } //eliminarProducto

    }// class ProductosService
