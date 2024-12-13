package com.sumativafs3.demo.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class CompraTest {

    private Compra compra;

    @BeforeEach
    void setUp() {
        compra = new Compra();
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(compra.getFechaCompra());
        assertEquals(0.0, compra.getTotal());
        assertEquals("PENDIENTE", compra.getEstadoCompra());
        assertNotNull(compra.getDetalles());
        assertTrue(compra.getDetalles().isEmpty());
    }

    @Test
    void testSetAndGetMethods() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.now();
        Usuario usuario = new Usuario();
        Double totalPrueba = 500.0;
        String estadoPrueba = "COMPLETADA";

        // Act
        compra.setFechaCompra(fecha);
        compra.setTotal(totalPrueba);
        compra.setEstadoCompra(estadoPrueba);
        compra.setUsuario(usuario);
        compra.setId(1L);

        // Assert
        assertEquals(fecha, compra.getFechaCompra());
        assertEquals(totalPrueba, compra.getTotal());
        assertEquals(estadoPrueba, compra.getEstadoCompra());
        assertEquals(usuario, compra.getUsuario());
        assertEquals(1L, compra.getId());
    }

    @Test
    void testSetAndGetDetalles() {
        // Arrange
        List<DetalleCompra> nuevosDetalles = new ArrayList<>();

        // Act
        compra.setDetalles(nuevosDetalles);

        // Assert
        assertEquals(nuevosDetalles, compra.getDetalles());
    }
}