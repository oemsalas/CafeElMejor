package com.cafe.cafeDemo.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.cafe.cafeDemo.service.FacturaPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@CrossOrigin
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired private FacturaRepository facturaRepository;
    @Autowired private FacturaPdfService facturaPdfService;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private CobranzaRepository cobranzaRepository;
    @Autowired private ProductosRepository productosRepository;

    @GetMapping("/listar")
    public Page<Factura> getAllFacturas(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("idFactura").descending());
        return search.isBlank()
                ? facturaRepository.findAll(pageable)
                : facturaRepository.search(search, pageable);
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
        factura.setFechaEmision(facturaDetalles.getFechaEmision());
        factura.setImpuestos(facturaDetalles.getImpuestos());
        factura.setTotal(facturaDetalles.getTotal());
        factura.setCliente(facturaDetalles.getCliente());
        factura.setCobranza(facturaDetalles.getCobranza());
        factura.setProductos(facturaDetalles.getProductos());
        factura.setEstado(facturaDetalles.getEstado());
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

    @PutMapping("/{id}/cobranza/{idCobranza}")
    public ResponseEntity<Factura> asignarCobranza(@PathVariable Long id, @PathVariable Long idCobranza) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        Cobranza cobranza = cobranzaRepository.findById(idCobranza)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrada con id " + idCobranza));
        factura.setCobranza(cobranza);
        factura.setEstado("PAGADA");
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    @DeleteMapping("/{id}/cobranza")
    public ResponseEntity<Factura> quitarCobranza(@PathVariable Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        factura.setCobranza(null);
        if (!"CANCELADA".equals(factura.getEstado())) factura.setEstado("PENDIENTE");
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

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

    // ── Endpoint PDF ──
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Long id) throws Exception {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        byte[] pdf = facturaPdfService.generarFacturaPdf(factura);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=factura-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private void resolverRelaciones(Factura factura) {
        if (factura.getCliente() != null) {
            Cliente cliente = clienteRepository.findById((long) factura.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente no encontrado con id " + factura.getCliente().getId()));
            factura.setCliente(cliente);
        }
        if (factura.getCobranza() != null && factura.getCobranza().getId() != 0) {
            Cobranza cobranza = cobranzaRepository.findById((long) factura.getCobranza().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cobranza no encontrada con id " + factura.getCobranza().getId()));
            factura.setCobranza(cobranza);
            factura.setEstado("PAGADA");
        } else {
            factura.setCobranza(null);
            if (!"CANCELADA".equals(factura.getEstado())) factura.setEstado("PENDIENTE");
        }
        if (factura.getProductos() != null && !factura.getProductos().isEmpty()) {
            List<Producto> productos = new ArrayList<>();
            for (Producto p : factura.getProductos()) {
                productos.add(productosRepository.findById((long) p.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado con id " + p.getId())));
            }
            factura.setProductos(productos);
        }
    }
}
