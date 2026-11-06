package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Producto;
import com.generation.tienda3v.services.AdminAuthService;
import com.generation.tienda3v.services.ProductoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private AdminAuthService adminAuthService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.obtenerTodos();
    }//listarProductos

    @GetMapping("/activos")
    public List<Producto> listarProductosActivos() {
        return productoService.obtenerActivos();
    }//listarProductosActivos

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }//obtenerPorId

    @GetMapping("/categoria/{idCategoria}")
    public List<Producto> listarPorCategoria(@PathVariable Long idCategoria) {
        return productoService.obtenerPorCategoria(idCategoria);
    }//listarPorCategoria

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@RequestBody Producto producto, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return productoService.guardar(producto);
    }//crearProducto

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return productoService.actualizar(id, producto);
    }//actualizarProducto

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        productoService.eliminar(id);
    }//eliminarProducto

}// class ProductoController
