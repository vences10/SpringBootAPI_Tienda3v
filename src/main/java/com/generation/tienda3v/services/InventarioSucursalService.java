package com.generation.tienda3v.services;

import com.generation.tienda3v.models.InventarioSucursal;
import com.generation.tienda3v.models.InventarioSucursalId;
import com.generation.tienda3v.repositories.InventarioSucursalRepository;
import com.generation.tienda3v.repositories.ProductoRepository;
import com.generation.tienda3v.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class InventarioSucursalService {

    private final InventarioSucursalRepository inventarioSucursalRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public InventarioSucursalService(InventarioSucursalRepository inventarioSucursalRepository,
                                     SucursalRepository sucursalRepository, ProductoRepository productoRepository) {
        this.inventarioSucursalRepository = inventarioSucursalRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
    }

    public List<InventarioSucursal> obtenerTodos() {
        return inventarioSucursalRepository.findAll();
    }

    public InventarioSucursal obtenerPorId(Long idSucursal, Long idProducto) {
        return inventarioSucursalRepository.findById(new InventarioSucursalId(idSucursal, idProducto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));
    }

    public InventarioSucursal crear(InventarioSucursal inventario) {
        InventarioSucursalId id = inventario.getId();
        if (id == null || id.getSucursalId() == null || id.getProductoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica los ids de sucursal y producto en id");
        }
        validarDatos(inventario, id);
        if (inventarioSucursalRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El inventario ya existe");
        }
        inventario.setSucursal(sucursalRepository.findById(id.getSucursalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada")));
        inventario.setProducto(productoRepository.findById(id.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")));
        return inventarioSucursalRepository.save(inventario);
    }

    public InventarioSucursal actualizar(Long idSucursal, Long idProducto, InventarioSucursal inventario) {
        InventarioSucursal existente = obtenerPorId(idSucursal, idProducto);
        InventarioSucursalId id = new InventarioSucursalId(idSucursal, idProducto);
        if (inventario.getId() != null && !id.equals(inventario.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id debe coincidir con la ruta");
        }
        validarDatos(inventario, id);
        existente.setExistencia(inventario.getExistencia());
        return inventarioSucursalRepository.save(existente);
    }

    public void eliminar(Long idSucursal, Long idProducto) {
        inventarioSucursalRepository.delete(obtenerPorId(idSucursal, idProducto));
    }

    private void validarDatos(InventarioSucursal inventario, InventarioSucursalId id) {
        if (inventario.getExistencia() == null || inventario.getExistencia() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La existencia debe ser mayor o igual a cero");
        }
        if (inventario.getSucursal() != null
                && !Objects.equals(inventario.getSucursal().getId(), id.getSucursalId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sucursal debe coincidir con el id");
        }
        if (inventario.getProducto() != null
                && !Objects.equals(inventario.getProducto().getIdProducto(), id.getProductoId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto debe coincidir con el id");
        }
    }
}
