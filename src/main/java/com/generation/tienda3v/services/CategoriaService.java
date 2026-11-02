package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Categoria;
import com.generation.tienda3v.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }//obtenerPorId

    public Optional<Categoria> obtenerPorSlug(String slug) {
        return categoriaRepository.findBySlug(slug);
    }//obtenerPorSlug

    public Categoria guardar(Categoria categoria) {
        if (categoria.getIdCategoria() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id se genera automáticamente");
        }
        return categoriaRepository.save(categoria);
    }//guardar

    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = obtenerPorId(id);
        existente.setNombre(categoria.getNombre());
        existente.setSlug(categoria.getSlug());
        existente.setActiva(categoria.getActiva());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        categoriaRepository.delete(obtenerPorId(id));
    }//eliminar

}// class CategoriaService
