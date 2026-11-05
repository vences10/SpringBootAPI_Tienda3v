package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Sucursal;
import com.generation.tienda3v.services.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sucursal> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Sucursal obtenerPorId(@PathVariable("id") Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sucursal crear(@RequestBody Sucursal sucursal) {
        return service.crear(sucursal);
    }

    @PutMapping("/{id}")
    public Sucursal actualizar(@PathVariable("id") Long id, @RequestBody Sucursal sucursal) {
        return service.actualizar(id, sucursal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
    }
}
