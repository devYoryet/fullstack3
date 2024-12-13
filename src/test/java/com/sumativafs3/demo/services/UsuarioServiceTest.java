package com.sumativafs3.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;
import com.sumativafs3.demo.services.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void getRolByUsuarioIdTest() {
        Long id = 1L;
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        String result = usuarioService.getRolByUsuarioId(id);
        assertEquals("ROLE_USER", result);
    }

    @Test
    void getRolByUsuarioEmailTest() {
        String email = "user@example.com";
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setRol(rol);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        String result = usuarioService.getRolByUsuarioEmail(email);
        assertEquals("ROLE_USER", result);
    }

    @Test
    void findByEmailTest() {
        String email = "user@example.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testRegistrarUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("Test123!");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        usuario.setRol(rol);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(rolRepository.findByNombre(anyString())).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario result = usuarioService.registrarUsuario(usuario);

        // Then
        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        verify(passwordEncoder).encode(anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testGetUsuarioById() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        Usuario result = usuarioService.getUsuarioById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testRegistrarUsuarioWithExistingEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("existing@email.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });
    }

    // UsuarioServiceTest.java - Añadir estos tests
    @Test
    void testUpdateUsuario() {
        Long id = 1L;
        Usuario existingUser = new Usuario();
        existingUser.setId(id);
        existingUser.setNombre("Old Name");
        existingUser.setEmail("old@email.com");

        Usuario updateDetails = new Usuario();
        updateDetails.setNombre("New Name");
        updateDetails.setEmail("new@email.com");
        updateDetails.setPassword("NewPass123!");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updateDetails);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Usuario result = usuarioService.updateUsuario(id, updateDetails);

        assertNotNull(result);
        assertEquals("New Name", result.getNombre());
        assertEquals("new@email.com", result.getEmail());
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void testDeleteUsuario() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        assertDoesNotThrow(() -> usuarioService.deleteUsuario(id));
        verify(usuarioRepository).delete(usuario);
    }
}