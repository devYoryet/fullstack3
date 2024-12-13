package com.sumativafs3.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.models.DetalleCompra;
import com.sumativafs3.demo.repositories.CompraRepository;
import com.sumativafs3.demo.repositories.DetalleCompraRepository;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {
    @Mock
    private CompraRepository compraRepository;
    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @InjectMocks
    private CompraService compraService;

    @Test
    void testCreateCompra() {
        // Given
        Compra compra = new Compra();
        compra.setId(1L);
        compra.setTotal(100.0);
        compra.setEstadoCompra("PENDIENTE");
        compra.setDetalles(new ArrayList<>()); // Inicializar la lista de detalles

        // Configurar el mock para aceptar múltiples llamadas a save
        when(compraRepository.save(any(Compra.class))).thenReturn(compra);

        // When
        Compra result = compraService.createCompra(compra);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDIENTE", result.getEstadoCompra());
        // Verificar que save fue llamado al menos una vez
        verify(compraRepository, atLeast(1)).save(any(Compra.class));
    }

    @Test
    void testGetAllCompras() {
        // Given
        List<Compra> compras = Arrays.asList(
                createCompra(1L, 100.0),
                createCompra(2L, 200.0));
        when(compraRepository.findAll()).thenReturn(compras);

        // When
        List<Compra> result = compraService.getAllCompras();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(compraRepository).findAll();
    }

    private Compra createCompra(Long id, Double total) {
        Compra compra = new Compra();
        compra.setId(id);
        compra.setTotal(total);
        compra.setEstadoCompra("PENDIENTE");
        return compra;
    }

    @Test
    void testCreateCompraWithEmptyDetails() {
        Compra compra = new Compra();
        when(compraRepository.save(any(Compra.class))).thenReturn(compra);

        Compra result = compraService.createCompra(compra);

        assertNotNull(result);
        assertEquals(0.0, result.getTotal());
    }

    @Test
    void testUpdateCompra() {
        // Given
        Long id = 1L;
        Compra existingCompra = new Compra();
        existingCompra.setId(id);
        existingCompra.setEstadoCompra("PENDIENTE");
        existingCompra.setDetalles(new ArrayList<>());

        Compra updatedCompra = new Compra();
        updatedCompra.setEstadoCompra("COMPLETADA");
        List<DetalleCompra> detalles = new ArrayList<>();
        DetalleCompra detalle = new DetalleCompra();
        detalle.setPrecioUnitario(100.0);
        detalle.setCantidad(2);
        detalles.add(detalle);
        updatedCompra.setDetalles(detalles);

        when(compraRepository.findById(id)).thenReturn(Optional.of(existingCompra));
        when(compraRepository.save(any(Compra.class))).thenReturn(updatedCompra);

        // When
        Compra result = compraService.updateCompra(id, updatedCompra);

        // Then
        verify(detalleCompraRepository).deleteAll(anyList());
        verify(compraRepository).save(any(Compra.class));
        assertEquals("COMPLETADA", result.getEstadoCompra());
    }

    @Test
    void testGetCompraById() {
        // Given
        Long id = 1L;
        Compra compra = new Compra();
        compra.setId(id);
        when(compraRepository.findById(id)).thenReturn(Optional.of(compra));

        // When
        Compra result = compraService.getCompraById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testGetCompraById_NotFound() {
        // Given
        Long id = 1L;
        when(compraRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> {
            compraService.getCompraById(id);
        });
    }

    @Test
    void testDeleteCompra() {
        // Given
        Long id = 1L;
        Compra compra = new Compra();
        compra.setId(id);
        when(compraRepository.findById(id)).thenReturn(Optional.of(compra));

        // When
        compraService.deleteCompra(id);

        // Then
        verify(compraRepository).delete(compra);
    }

    @Test
    void testGetComprasByUsuarioId() {
        // Given
        Long userId = 1L;
        List<Compra> compras = Arrays.asList(new Compra(), new Compra());
        when(compraRepository.findByUsuarioId(userId)).thenReturn(compras);

        // When
        List<Compra> result = compraService.getComprasByUsuarioId(userId);

        // Then
        assertEquals(2, result.size());
        verify(compraRepository).findByUsuarioId(userId);
    }

    @Test
    void testGetComprasByEstado() {
        // Given
        String estado = "PENDIENTE";
        List<Compra> compras = Arrays.asList(new Compra(), new Compra());
        when(compraRepository.findByEstadoCompra(estado)).thenReturn(compras);

        // When
        List<Compra> result = compraService.getComprasByEstado(estado);

        // Then
        assertEquals(2, result.size());
        verify(compraRepository).findByEstadoCompra(estado);
    }

    @Test
    void testGetAllCompras_Empty() {
        // Given
        when(compraRepository.findAll()).thenReturn(new ArrayList<>());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> {
            compraService.getAllCompras();
        });
    }
}