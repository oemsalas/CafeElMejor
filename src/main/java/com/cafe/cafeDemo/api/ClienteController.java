package com.cafe.cafeDemo.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Cliente;
import com.cafe.cafeDemo.repository.ClienteRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/listar")
    public Page<Cliente> getAllClientes(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return search.isBlank()
                ? clienteRepository.findAll(pageable)
                : clienteRepository.search(search, pageable);
    }

    // Endpoint sin paginar para usar en dropdown de Factura
    @GetMapping("/todos")
    public List<Cliente> getTodosClientes() {
        return clienteRepository.findAll(Sort.by("nombre"));
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetalles) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        cliente.setDireccion(clienteDetalles.getDireccion());
        cliente.setDni(clienteDetalles.getDni());
        cliente.setFechaCreacion(clienteDetalles.getFechaCreacion());
        cliente.setNombre(clienteDetalles.getNombre());
        cliente.setTelefono(clienteDetalles.getTelefono());
        return ResponseEntity.ok(clienteRepository.save(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        clienteRepository.delete(cliente);
        return ResponseEntity.noContent().build();
    }
}
