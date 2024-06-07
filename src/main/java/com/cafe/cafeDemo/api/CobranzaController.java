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
import com.cafe.cafeDemo.model.Cobranza;
import com.cafe.cafeDemo.repository.CobranzaRepository;


@CrossOrigin//(origins = "http://localhost:8080",allowedHeaders = {"Authorization", "Origin"},allowCredentials="true",exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"})
@RestController
@RequestMapping("/api/cobranza")
public class CobranzaController {

    @Autowired
    private CobranzaRepository cobranzaRepository;
    
    @GetMapping(value="/listar")
    public List<Cobranza> getAllProductos() {
        return cobranzaRepository.findAll();
    }

    @PostMapping
    public Cobranza createProducto(@RequestBody Cobranza cobranza) {
        return cobranzaRepository.save(cobranza);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cobranza> getProductoById(@PathVariable Long id) {
    	Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrado con id " + id));
        return ResponseEntity.ok(cobranza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cobranza> updateProducto(@PathVariable Long id, @RequestBody Cobranza cobranzaDetalles) {
        Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrado con id " + id));

        cobranza.setFechaCobranza(cobranzaDetalles.getFechaCobranza());
        cobranza.setMetodoDePago(cobranzaDetalles.getMetodoDePago());
        cobranza.setMonto(cobranzaDetalles.getMonto());

        Cobranza updatedCobranza = cobranzaRepository.save(cobranza);
        return ResponseEntity.ok(updatedCobranza);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCobranza(@PathVariable Long id) {
        Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrado con id " + id));

        cobranzaRepository.delete(cobranza);
        return ResponseEntity.noContent().build();
    }
}
