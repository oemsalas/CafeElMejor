package com.cafe.cafeDemo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Factura;
import com.cafe.cafeDemo.repository.FacturaRepository;


@CrossOrigin//(origins = "http://localhost:8080",allowedHeaders = {"Authorization", "Origin"},allowCredentials="true",exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"})
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;
    
    @GetMapping(value="/listar")
    public List<Factura> getAllProductos() {
        return facturaRepository.findAll();
    }

    @PostMapping
    public Factura createProducto(@RequestBody Factura factura) {
        return facturaRepository.save(factura);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getProductoById(@PathVariable Long id) {
    	Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrado con id " + id));
        return ResponseEntity.ok(factura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> updateProducto(@PathVariable Long id, @RequestBody Factura facturaDetalles) {
    	Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrado con id " + id));

        factura.setCobranza(facturaDetalles.getCobranza());
        factura.setEstado(facturaDetalles.getEstado());
        factura.setFechaEmision(facturaDetalles.getFechaEmision());
        factura.setIdCliente(facturaDetalles.getIdCliente());
        factura.setImpuestos(facturaDetalles.getImpuestos());
        factura.setTotal(facturaDetalles.getTotal());
        
        Factura updatedFactura = facturaRepository.save(factura);
        return ResponseEntity.ok(updatedFactura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
    	Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrado con id " + id));

        facturaRepository.delete(factura);
        return ResponseEntity.noContent().build();
    }
}
