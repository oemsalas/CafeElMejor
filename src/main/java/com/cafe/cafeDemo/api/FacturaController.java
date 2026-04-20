package com.cafe.cafeDemo.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Cliente;
import com.cafe.cafeDemo.model.Cobranza;
import com.cafe.cafeDemo.model.Factura;
import com.cafe.cafeDemo.model.Producto;
import com.cafe.cafeDemo.repository.ClienteRepository;
import com.cafe.cafeDemo.repository.CobranzaRepository;
import com.cafe.cafeDemo.repository.FacturaRepository;
import com.cafe.cafeDemo.repository.ProductosRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired private FacturaRepository facturaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private CobranzaRepository cobranzaRepository;
    @Autowired private ProductosRepository productosRepository;

    @GetMapping("/listar")
    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    @PostMapping
    public Factura createFactura(@RequestBody Factura factura) {
        resolverRelaciones(factura);
        return facturaRepository.save(factura);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getFacturaById(@PathVariable Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        return ResponseEntity.ok(factura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> updateFactura(@PathVariable Long id, @RequestBody Factura facturaDetalles) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        factura.setEstado(facturaDetalles.getEstado());
        factura.setFechaEmision(facturaDetalles.getFechaEmision());
        factura.setImpuestos(facturaDetalles.getImpuestos());
        factura.setTotal(facturaDetalles.getTotal());
        factura.setCliente(facturaDetalles.getCliente());
        factura.setCobranza(facturaDetalles.getCobranza());
        factura.setProductos(facturaDetalles.getProductos());
        resolverRelaciones(factura);
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        facturaRepository.delete(factura);
        return ResponseEntity.noContent().build();
    }

    // ── Endpoints adicionales para manejar productos de una factura ──

    @PostMapping("/{id}/producto/{idProducto}")
    public ResponseEntity<Factura> addProductoToFactura(@PathVariable Long id, @PathVariable Long idProducto) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        Producto producto = productosRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + idProducto));
        if (factura.getProductos() == null) factura.setProductos(new ArrayList<>());
        factura.getProductos().add(producto);
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    @DeleteMapping("/{id}/producto/{idProducto}")
    public ResponseEntity<Factura> removeProductoFromFactura(@PathVariable Long id, @PathVariable Long idProducto) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        if (factura.getProductos() != null)
            factura.getProductos().removeIf(p -> p.getId() == idProducto.intValue());
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    // ── Helper para resolver relaciones desde los IDs recibidos ──
    private void resolverRelaciones(Factura factura) {
        // Cliente (obligatorio)
        if (factura.getCliente() != null) {
            Cliente cliente = clienteRepository.findById((long) factura.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente no encontrado con id " + factura.getCliente().getId()));
            factura.setCliente(cliente);
        }
        // Cobranza (opcional)
        if (factura.getCobranza() != null && factura.getCobranza().getId() != 0) {
            Cobranza cobranza = cobranzaRepository.findById((long) factura.getCobranza().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cobranza no encontrada con id " + factura.getCobranza().getId()));
            factura.setCobranza(cobranza);
        } else {
            factura.setCobranza(null);
        }
        // Productos (opcional, lista)
        if (factura.getProductos() != null && !factura.getProductos().isEmpty()) {
            List<Producto> productos = new ArrayList<>();
            for (Producto p : factura.getProductos()) {
                Producto prod = productosRepository.findById((long) p.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado con id " + p.getId()));
                productos.add(prod);
            }
            factura.setProductos(productos);
        }
    }
}
