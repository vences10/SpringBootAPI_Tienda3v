package com.generation.tienda3v.services;

import com.generation.tienda3v.models.Categoria;
import com.generation.tienda3v.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriasService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //Obtener todas las categorías - GET
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }//obtenerCategorias

    //obtener categoria por ID - GET
    public Optional<Categoria> obtenerCategoriaPorId(Long id){
        return categoriaRepository.findById(id);
    }//obtenerCategoriaPorId

    //guardar o actualizar una categoria - POST/PUT
    public  Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }//guardar

    //eliminar una categoría - DELETE
    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }//eliminar


}
