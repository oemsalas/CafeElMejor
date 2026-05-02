package com.cafe.cafeDemo.api;

import com.cafe.cafeDemo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Usuario;
import com.cafe.cafeDemo.repository.UsuarioRepository;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<?> login(@RequestBody Usuario body) {
        System.out.println(">>> BUSCANDO: " + body.getUsuario());
        Usuario usuario = usuarioRepository.findByUsuario(body.getUsuario());
        System.out.println(">>> ENCONTRADO: " + (usuario != null ? usuario.getUsuario() : "NULL"));
        if (usuario != null) {
            System.out.println(">>> PASSWORD BD: " + usuario.getPassword());
            System.out.println(">>> MATCHES: " + passwordEncoder.matches(body.getPassword(), usuario.getPassword()));
        }
        if (usuario == null || !passwordEncoder.matches(body.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
        String token = jwtUtil.generateToken(usuario.getUsuario());
        return ResponseEntity.ok(Map.of(
                "token",          token,
                "id",             usuario.getId(),
                "nombreApellido", usuario.getNombreApellido(),
                "usuario",        usuario.getUsuario()
        ));
    }

    @PostMapping
    public Usuario addUsuario(@RequestBody Usuario nuevoUsuario) {
        // Hashear la contraseña antes de guardar
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        return usuarioRepository.save(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id,
                                                  @RequestBody Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        usuario.setNombreApellido(usuarioDetalles.getNombreApellido());
        usuario.setUsuario(usuarioDetalles.getUsuario());
        // Solo re-hashear si mandaron una contraseña nueva
        if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDetalles.getPassword()));
        }
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
