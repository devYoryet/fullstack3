package com.sumativafs3.demo.controllers;

import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.services.RolService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RolControllerTest {

    @Mock
    private RolService rolService;

    @InjectMocks
    private RolController rolController;

    @Test
    void testGetAllRoles() {
        // Mock data
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");

        // Mock behavior
        when(rolService.getAllRoles()).thenReturn(Collections.singletonList(rol));

        // Test method
        ResponseEntity<List<Rol>> response = rolController.getAllRoles();

        // Assertions
        assertEquals(1, response.getBody().size());
        assertEquals("ROLE_USER", response.getBody().get(0).getNombre());
    }

    @Test
    void testCreateRol() {
        // Mock data
        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");

        // Mock behavior
        when(rolService.createRol(any(Rol.class))).thenReturn(rol);

        // Test method
        ResponseEntity<?> response = rolController.createRol(rol);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteRol() {
        doNothing().when(rolService).deleteRol(anyLong());

        ResponseEntity<?> response = rolController.deleteRol(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rolService, times(1)).deleteRol(1L);
    }
}
