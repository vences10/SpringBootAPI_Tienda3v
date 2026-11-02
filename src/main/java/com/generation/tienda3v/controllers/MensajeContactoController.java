package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.MensajeContacto;
import com.generation.tienda3v.services.MensajeContactoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes-contacto")
@CrossOrigin(origins = "*")
public class MensajeContactoController {

    private final MensajeContactoService service;

    public MensajeContactoController(MensajeContactoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MensajeContacto> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public MensajeContacto obtenerPorId(@PathVariable("id") Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeContacto crear(@RequestBody MensajeContacto mensaje) {
        return service.crear(mensaje);
    }

    @PutMapping("/{id}")
    public MensajeContacto actualizar(@PathVariable("id") Long id, @RequestBody MensajeContacto mensaje) {
        return service.actualizar(id, mensaje);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
    }
}
