package com.generation.tienda3v.controllers;

import com.generation.tienda3v.models.InventarioSucursal;
import com.generation.tienda3v.services.InventarioSucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario-sucursal")
@CrossOrigin(origins = "*")
public class InventarioSucursalController {

    private final InventarioSucursalService inventarioSucursalService;

    @Autowired
    public InventarioSucursalController(InventarioSucursalService inventarioSucursalService) {
        this.inventarioSucursalService = inventarioSucursalService;
    }

    @GetMapping
    public List<InventarioSucursal> listarInventario() {
        return inventarioSucursalService.obtenerTodos();
    }

    @GetMapping("/{idSucursal}/{idProducto}")
    public InventarioSucursal obtenerPorId(@PathVariable Long idSucursal, @PathVariable Long idProducto) {
        return inventarioSucursalService.obtenerPorId(idSucursal, idProducto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioSucursal crearInventario(@RequestBody InventarioSucursal inventario) {
        return inventarioSucursalService.crear(inventario);
    }

    @PutMapping("/{idSucursal}/{idProducto}")
    public InventarioSucursal actualizarInventario(@PathVariable Long idSucursal, @PathVariable Long idProducto,
                                                  @RequestBody InventarioSucursal inventario) {
        return inventarioSucursalService.actualizar(idSucursal, idProducto, inventario);
    }

    @DeleteMapping("/{idSucursal}/{idProducto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long idSucursal, @PathVariable Long idProducto) {
        inventarioSucursalService.eliminar(idSucursal, idProducto);
    }
}
