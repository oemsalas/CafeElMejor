package com.cafe.cafeDemo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.cafe.cafeDemo.api.UsuarioController;
import com.cafe.cafeDemo.exception.ResourceNotFoundException;
import com.cafe.cafeDemo.model.Usuario;
import com.cafe.cafeDemo.repository.UsuarioRepository;

class CafeDemoApplicationTests {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombreApellido("Omar Salas");
        usuario.setUsuario("omar");
        usuario.setPassword("password123");
    }

    @Test
    void testGetUsuarios() {
        // Preparar datos simulados
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // Ejecutar el controlador
        List<Usuario> usuarios = usuarioController.getUsuarios();

        // Verificar que el controlador devuelve los usuarios esperados
        verify(usuarioRepository).findAll();
        assert usuarios.size() == 1;
        assert usuarios.get(0).getNombreApellido().equals("Omar Salas");
    }

    @Test
    void testGetUsuarioById() {
        // Preparar datos simulados
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        // Ejecutar el controlador
        ResponseEntity<Usuario> response = usuarioController.getUsuarioById(1L);

        // Verificar el resultado
        verify(usuarioRepository).findById(1L);
        assert response.getBody().getNombreApellido().equals("Omar Salas");
    }

    @Test
    void testGetUsuarioById_NotFound() {
        // Simular que el usuario no existe
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ejecutar el controlador y verificar que lanza la excepción esperada
        try {
            usuarioController.getUsuarioById(1L);
        } catch (ResourceNotFoundException e) {
            assert e.getMessage().contains("Usuario no encontrado");
        }

        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testAddUsuario() {
        // Preparar datos simulados
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar el controlador
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreApellido("Nuevo Usuario");
        nuevoUsuario.setUsuario("nuevo");
        nuevoUsuario.setPassword("newpassword");

        Usuario result = usuarioController.addUsuario(nuevoUsuario);

        // Verificar el resultado
        verify(usuarioRepository).save(nuevoUsuario);
        assert result.getNombreApellido().equals("Omar Salas");
    }

    @Test
    void testUpdateUsuario() {
        // Preparar datos simulados
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar el controlador
        Usuario usuarioDetalles = new Usuario();
        usuarioDetalles.setNombreApellido("Omar Modificado");
        usuarioDetalles.setUsuario("omar");
        usuarioDetalles.setPassword("newpassword");

        ResponseEntity<Usuario> response = usuarioController.updateUsuario(1L, usuarioDetalles);

        // Verificar el resultado
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
        assert response.getBody().getNombreApellido().equals("Omar Modificado");
    }

    @Test
    void testDeleteUsuario() {
        // Preparar datos simulados
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        // Ejecutar el controlador
        ResponseEntity<Void> response = usuarioController.deleteUsuario(1L);

        // Verificar que se llamó al método de eliminación
        verify(usuarioRepository).delete(usuario);
        assert response.getStatusCodeValue() == 204;
    }
}
