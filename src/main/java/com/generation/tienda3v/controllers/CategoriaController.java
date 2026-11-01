package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Categoria;
import com.generation.tienda3v.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.obtenerTodas();
    }//listarCategorias

    @GetMapping("/activas")
    public List<Categoria> listarCategoriasActivas() {
        return categoriaService.obtenerActivas();
    }//listarCategoriasActivas

    @GetMapping("/{id}")
    public Optional<Categoria> obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }//obtenerPorId

    @PostMapping
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.guardar(categoria);
    }//crearCategoria

    @PutMapping("/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setIdCategoria(id);
        return categoriaService.guardar(categoria);
    }//actualizarCategoria

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }//eliminarCategoria

}// class CategoriaController