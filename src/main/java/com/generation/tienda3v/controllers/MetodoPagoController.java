package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.MetodoPago;
import com.generation.tienda3v.services.MetodoPagoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
public class MetodoPagoController {

    private final MetodoPagoService service;

    public MetodoPagoController(MetodoPagoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MetodoPago> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public MetodoPago obtenerPorId(@PathVariable("id") Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetodoPago crear(@RequestBody MetodoPago metodoPago) {
        return service.crear(metodoPago);
    }

    @PutMapping("/{id}")
    public MetodoPago actualizar(@PathVariable("id") Long id, @RequestBody MetodoPago metodoPago) {
        return service.actualizar(id, metodoPago);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
    }
}
