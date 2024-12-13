package com.sumativafs3.demo.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {
    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void getAllRoles_Success() {
        // Given
        List<Rol> roles = Arrays.asList(
                createRol(1L, "ROLE_ADMIN"),
                createRol(2L, "ROLE_USER"));
        when(rolRepository.findAll()).thenReturn(roles);

        // When
        List<Rol> result = rolService.getAllRoles();

        // Then
        assertEquals(2, result.size());
        verify(rolRepository).findAll();
    }

    @Test
    void getAllRoles_EmptyList() {
        // Given
        when(rolRepository.findAll()).thenReturn(Collections.emptyList());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> {
            rolService.getAllRoles();
        });
    }

    @Test
    void getRolById_Success() {
        // Given
        Long id = 1L;
        Rol rol = createRol(id, "ROLE_ADMIN");
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));

        // When
        Rol result = rolService.getRolById(id);

        // Then
        assertEquals(id, result.getId());
        assertEquals("ROLE_ADMIN", result.getNombre());
    }

    @Test
    void getRolById_NotFound() {
        // Given
        Long id = 1L;
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> {
            rolService.getRolById(id);
        });
    }

    @Test
    void getRolByNombre_Success() {
        // Given
        String nombre = "ROLE_ADMIN";
        Rol rol = createRol(1L, nombre);
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.of(rol));

        // When
        Rol result = rolService.getRolByNombre(nombre);

        // Then
        assertEquals(nombre, result.getNombre());
    }

    @Test
    void getRolByNombre_NotFound() {
        // Given
        String nombre = "ROLE_NOT_EXISTS";
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> {
            rolService.getRolByNombre(nombre);
        });
    }

    @Test
    void createRol_Success() {
        // Given
        Rol rol = createRol(null, "ROLE_NEW");
        Rol savedRol = createRol(1L, "ROLE_NEW");
        when(rolRepository.save(rol)).thenReturn(savedRol);

        // When
        Rol result = rolService.createRol(rol);

        // Then
        assertNotNull(result);
        assertEquals("ROLE_NEW", result.getNombre());
        verify(rolRepository).save(rol);
    }

    @Test
    void deleteRol_Success() {
        // Given
        Long id = 1L;
        Rol rol = createRol(id, "ROLE_ADMIN");
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));
        when(usuarioRepository.existsByRolId(id)).thenReturn(false);

        // When
        assertDoesNotThrow(() -> rolService.deleteRol(id));

        // Then
        verify(rolRepository).delete(rol);
    }

    @Test
    void deleteRol_RoleInUse() {
        // Given
        Long id = 1L;
        Rol rol = createRol(id, "ROLE_ADMIN");
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));
        when(usuarioRepository.existsByRolId(id)).thenReturn(true);

        // When/Then
        assertThrows(RuntimeException.class, () -> {
            rolService.deleteRol(id);
        });
    }

    @Test
    void deleteRol_NotFound() {
        // Given
        Long id = 1L;
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> {
            rolService.deleteRol(id);
        });
    }

    private Rol createRol(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        return rol;
    }
}
