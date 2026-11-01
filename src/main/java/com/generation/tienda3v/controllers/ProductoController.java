package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Producto;
import com.generation.tienda3v.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.obtenerTodos();
    }//listarProductos

    @GetMapping("/activos")
    public List<Producto> listarProductosActivos() {
        return productoService.obtenerActivos();
    }//listarProductosActivos

    @GetMapping("/{id}")
    public Optional<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }//obtenerPorId

    @GetMapping("/categoria/{idCategoria}")
    public List<Producto> listarPorCategoria(@PathVariable Long idCategoria) {
        return productoService.obtenerPorCategoria(idCategoria);
    }//listarPorCategoria

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }//crearProducto

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setIdProducto(id);
        return productoService.guardar(producto);
    }//actualizarProducto

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }//eliminarProducto

}// class ProductoController