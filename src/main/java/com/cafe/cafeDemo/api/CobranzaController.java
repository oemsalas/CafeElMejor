package com.cafe.cafeDemo.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Cobranza;
import com.cafe.cafeDemo.repository.CobranzaRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/cobranza")
public class CobranzaController {

    @Autowired
    private CobranzaRepository cobranzaRepository;

    @GetMapping("/listar")
    public List<Cobranza> getAllCobranzas() {
        return cobranzaRepository.findAll();
    }

    @PostMapping
    public Cobranza createCobranza(@RequestBody Cobranza cobranza) {
        return cobranzaRepository.save(cobranza);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cobranza> getCobranzaById(@PathVariable Long id) {
        Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrada con id " + id));
        return ResponseEntity.ok(cobranza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cobranza> updateCobranza(@PathVariable Long id, @RequestBody Cobranza cobranzaDetalles) {
        Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrada con id " + id));
        cobranza.setFechaCobranza(cobranzaDetalles.getFechaCobranza());
        cobranza.setMetodoDePago(cobranzaDetalles.getMetodoDePago());
        cobranza.setMonto(cobranzaDetalles.getMonto());
        return ResponseEntity.ok(cobranzaRepository.save(cobranza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCobranza(@PathVariable Long id) {
        Cobranza cobranza = cobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrada con id " + id));
        cobranzaRepository.delete(cobranza);
        return ResponseEntity.noContent().build();
    }
}
