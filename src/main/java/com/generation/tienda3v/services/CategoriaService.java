package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Categoria;
import com.generation.tienda3v.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }//obtenerTodas

    public List<Categoria> obtenerActivas() {
        return categoriaRepository.findByActivaTrue();
    }//obtenerActivas

    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }//obtenerPorId

    public Optional<Categoria> obtenerPorSlug(String slug) {
        return categoriaRepository.findBySlug(slug);
    }//obtenerPorSlug

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }//guardar

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }//eliminar

}// class CategoriaService