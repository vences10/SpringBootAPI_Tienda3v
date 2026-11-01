package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Categoria;
import com.generation.tienda3v.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/3V/categorias")
@CrossOrigin(origins = "*")
public class CategoriasController {

    @Autowired
    private CategoriaService categoriasService;

    @GetMapping
    public List<Categoria> listarCategorias(){
        return categoriasService.obtenerCategorias();
    } //listarCategorias

    @GetMapping("/{id}")
    public Optional<Categoria> obtenerCategoriaPorId(@PathVariable Integer id){
        return categoriasService.obtenerCategoriaPorId(id);
    }
}// class CategoriasController
