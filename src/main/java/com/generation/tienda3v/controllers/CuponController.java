package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.Cupon;
import com.generation.tienda3v.services.AdminAuthService;
import com.generation.tienda3v.services.CuponService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupones")
@CrossOrigin(origins = "*")
public class CuponController {

    private final CuponService service;
    private final AdminAuthService adminAuthService;

    public CuponController(CuponService service, AdminAuthService adminAuthService) {
        this.service = service;
        this.adminAuthService = adminAuthService;
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
    public Cupon crear(@RequestBody Cupon cupon, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return service.crear(cupon);
    }

    @PutMapping("/{id}")
    public Cupon actualizar(@PathVariable("id") Long id, @RequestBody Cupon cupon, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        return service.actualizar(id, cupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id, HttpServletRequest request) {
        adminAuthService.verificarAdmin(request);
        service.eliminar(id);
    }
}
