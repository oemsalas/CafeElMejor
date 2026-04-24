package com.cafe.cafeDemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Proveedor;
import com.cafe.cafeDemo.repository.ProveedorRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/listar")
    public Page<Proveedor> getAllProveedores(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return search.isBlank()
                ? proveedorRepository.findAll(pageable)
                : proveedorRepository.search(search, pageable);
    }

    @PostMapping
    public Proveedor createProveedor(@RequestBody Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id " + id));
        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable Long id, @RequestBody Proveedor proveedorDetalles) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id " + id));
        proveedor.setNombre(proveedorDetalles.getNombre());
        proveedor.setContacto(proveedorDetalles.getContacto());
        proveedor.setTelefono(proveedorDetalles.getTelefono());
        proveedor.setDireccion(proveedorDetalles.getDireccion());
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id " + id));
        proveedorRepository.delete(proveedor);
        return ResponseEntity.noContent().build();
    }
}
