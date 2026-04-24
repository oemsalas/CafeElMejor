package com.cafe.cafeDemo.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<Cobranza> getAllCobranzas(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return search.isBlank()
                ? cobranzaRepository.findAll(pageable)
                : cobranzaRepository.search(search, pageable);
    }

    // Endpoint sin paginar para usar en dropdowns de Factura
    @GetMapping("/todas")
    public List<Cobranza> getTodasCobranzas() {
        return cobranzaRepository.findAll(Sort.by("id").descending());
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
