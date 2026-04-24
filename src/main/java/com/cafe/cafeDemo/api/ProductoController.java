package com.cafe.cafeDemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Producto;
import com.cafe.cafeDemo.model.Proveedor;
import com.cafe.cafeDemo.repository.ProductosRepository;
import com.cafe.cafeDemo.repository.ProveedorRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private ProductosRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/listar")
    public Page<Producto> getAllProductos(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return search.isBlank()
                ? productoRepository.findAll(pageable)
                : productoRepository.search(search, pageable);
    }

    // Endpoint sin paginar para usar en dropdowns/selects del frontend
    @GetMapping("/todos")
    public java.util.List<Producto> getTodosProductos() {
        return productoRepository.findAll(Sort.by("descripcion"));
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        if (producto.getProveedor() != null && producto.getProveedor().getId() != 0) {
            Proveedor proveedor = proveedorRepository.findById((long) producto.getProveedor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Proveedor no encontrado con id " + producto.getProveedor().getId()));
            producto.setProveedor(proveedor);
        }
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
        producto.setDescripcion(productoDetalles.getDescripcion());
        producto.setFechaVencimiento(productoDetalles.getFechaVencimiento());
        producto.setLote(productoDetalles.getLote());
        producto.setPrecioVenta(productoDetalles.getPrecioVenta());
        if (productoDetalles.getProveedor() != null && productoDetalles.getProveedor().getId() != 0) {
            Proveedor proveedor = proveedorRepository.findById((long) productoDetalles.getProveedor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Proveedor no encontrado con id " + productoDetalles.getProveedor().getId()));
            producto.setProveedor(proveedor);
        } else {
            producto.setProveedor(null);
        }
        return ResponseEntity.ok(productoRepository.save(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
        productoRepository.delete(producto);
        return ResponseEntity.noContent().build();
    }
}
