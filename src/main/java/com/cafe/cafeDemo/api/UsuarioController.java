package com.cafe.cafeDemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Usuario;
import com.cafe.cafeDemo.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public Page<Usuario> getUsuarios(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "8")  int size,
            @RequestParam(defaultValue = "")   String search) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return search.isBlank()
                ? usuarioRepository.findAll(pageable)
                : usuarioRepository.search(search, pageable);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioRepository.getUsuarioByPassword(usuario.getUsuario(), usuario.getPassword());
    }

    @PostMapping
    public Usuario addUsuario(@RequestBody Usuario nuevoUsuario) {
        return usuarioRepository.save(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        usuario.setNombreApellido(usuarioDetalles.getNombreApellido());
        usuario.setPassword(usuarioDetalles.getPassword());
        usuario.setUsuario(usuarioDetalles.getUsuario());
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
