package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Cupon;
import com.generation.tienda3v.services.CuponService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupones")
@CrossOrigin(origins = "*")
public class CuponController {

    private final CuponService service;

    public CuponController(CuponService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cupon> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Cupon obtenerPorId(@PathVariable("id") Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cupon crear(@RequestBody Cupon cupon) {
        return service.crear(cupon);
    }

    @PutMapping("/{id}")
    public Cupon actualizar(@PathVariable("id") Long id, @RequestBody Cupon cupon) {
        return service.actualizar(id, cupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
    }
}
