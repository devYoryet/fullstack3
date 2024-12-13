package com.sumativafs3.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.services.UsuarioService;

@ExtendWith(MockitoExtension.class)

public class UsuarioControllerTest {
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void testGetAllUsuarios() {
        // Given
        List<Usuario> usuarios = Arrays.asList(
                createUsuario(1L, "User1", "user1@test.com"),
                createUsuario(2L, "User2", "user2@test.com"));
        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);

        // When
        List<Usuario> result = usuarioController.getAllUsuarios();

        // Then
        assertEquals(2, result.size());
        verify(usuarioService).getAllUsuarios();
    }

    @Test
    void testGetUsuarioById() {
        // Given
        Long id = 1L;
        Usuario usuario = createUsuario(id, "Test", "test@test.com");
        when(usuarioService.getUsuarioById(id)).thenReturn(usuario);

        // When
        Usuario result = usuarioController.getUsuarioById(id);

        // Then
        assertEquals(id, result.getId());
        assertEquals("Test", result.getNombre());
    }

    @Test
    void testUpdateUsuario() {
        // Given
        Long id = 1L;
        Usuario usuario = createUsuario(id, "Updated", "updated@test.com");
        when(usuarioService.updateUsuario(eq(id), any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario result = usuarioController.updateUsuario(id, usuario);

        // Then
        assertEquals("Updated", result.getNombre());
        verify(usuarioService).updateUsuario(id, usuario);
    }

    @Test
    void testDeleteUsuario() {
        // Given
        Long id = 1L;
        doNothing().when(usuarioService).deleteUsuario(id);

        // When
        usuarioController.deleteUsuario(id);

        // Then
        verify(usuarioService).deleteUsuario(id);
    }

    @Test
    void testGetRolByUsuarioId() {
        // Given
        Long id = 1L;
        String rolNombre = "ROLE_USER";
        when(usuarioService.getRolByUsuarioId(id)).thenReturn(rolNombre);

        // When
        ResponseEntity<String> response = usuarioController.getRolByUsuarioId(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rolNombre, response.getBody());
    }

    @Test
    void testGetRolByUsuarioEmail() {
        // Given
        String email = "test@test.com";
        String rolNombre = "ROLE_USER";
        when(usuarioService.getRolByUsuarioEmail(email)).thenReturn(rolNombre);

        // When
        ResponseEntity<String> response = usuarioController.getRolByUsuarioEmail(email);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rolNombre, response.getBody());
    }

    private Usuario createUsuario(Long id, String nombre, String email) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        return usuario;
    }
}
