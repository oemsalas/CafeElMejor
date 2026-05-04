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
        Usuario usuario = usuarioRepository.findByUsuario(body.getUsuario());
        if (usuario == null || !passwordEncoder.matches(body.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos"));
        }

        // Incrementar tokenVersion — invalida cualquier sesión anterior
        usuario.setTokenVersion(usuario.getTokenVersion() + 1);
        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuario.getUsuario(),
                usuario.getRol(),
                usuario.getTokenVersion()
        );

        return ResponseEntity.ok(Map.of(
                "token",          token,
                "id",             usuario.getId(),
                "nombreApellido", usuario.getNombreApellido(),
                "usuario",        usuario.getUsuario(),
                "rol",            usuario.getRol() != null ? usuario.getRol() : "OPERADOR"
        ));
    }

    @PostMapping
    public Usuario addUsuario(@RequestBody Usuario nuevoUsuario) {
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        if (nuevoUsuario.getRol() == null || nuevoUsuario.getRol().isBlank()) {
            nuevoUsuario.setRol("OPERADOR");
        }
        nuevoUsuario.setTokenVersion(0);
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
        if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDetalles.getPassword()));
            // Cambio de contraseña también invalida sesiones activas
            usuario.setTokenVersion(usuario.getTokenVersion() + 1);
        }
        if (usuarioDetalles.getRol() != null && !usuarioDetalles.getRol().isBlank()) {
            usuario.setRol(usuarioDetalles.getRol());
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
