package com.sumativafs3.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Producto;
import com.sumativafs3.demo.repositories.ProductoRepository;
import com.sumativafs3.demo.services.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testUpdateProducto() {
        // Given
        Long id = 1L;
        Producto existingProducto = createProducto(id, "Producto Original", 100.0);
        Producto updatedDetails = createProducto(id, "Producto Actualizado", 150.0);

        when(productoRepository.findById(id)).thenReturn(Optional.of(existingProducto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updatedDetails);

        // When
        Producto result = productoService.updateProducto(id, updatedDetails);

        // Then
        assertEquals("Producto Actualizado", result.getNombre());
        assertEquals(150.0, result.getPrecio());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testActualizarStockDespuesDeCompra() {
        // Given
        Long id = 1L;
        Producto producto = createProducto(id, "Test", 100.0);
        producto.setStock(10);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        productoService.actualizarStockDespuesDeCompra(id, 5);

        // Then
        assertEquals(5, producto.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void testActualizarStockDespuesDeCompra_StockInsuficiente() {
        // Given
        Long id = 1L;
        Producto producto = createProducto(id, "Test", 100.0);
        producto.setStock(5);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // When/Then
        assertThrows(IllegalStateException.class, () -> {
            productoService.actualizarStockDespuesDeCompra(id, 10);
        });
    }

    @Test
    void testGetAllProductos() {
        // Given
        List<Producto> productos = Arrays.asList(
                createProducto(1L, "Producto 1", 100.0),
                createProducto(2L, "Producto 2", 200.0));
        when(productoRepository.findAll()).thenReturn(productos);

        // When
        List<Producto> result = productoService.getAllProductos();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testCreateProducto() {
        // Given
        Producto producto = createProducto(1L, "Nuevo Producto", 100.0);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto result = productoService.createProducto(producto);

        // Then
        assertNotNull(result);
        assertEquals("Nuevo Producto", result.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testGetProductoById() {
        // Given
        Producto producto = createProducto(1L, "Producto 1", 100.0);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Producto result = productoService.getProductoById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductoById_NotFound() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.getProductoById(1L);
        });
    }

    private Producto createProducto(Long id, String nombre, Double precio) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(10);
        return producto;
    }

    // ProductoServiceTest.java - Añadir estos tests de alto impacto
    @Test
    void testGetProductosDisponibles() {
        List<Producto> productos = Arrays.asList(
                createProducto(1L, "P1", 100.0),
                createProducto(2L, "P2", 200.0));
        when(productoRepository.findByStockGreaterThan(0)).thenReturn(productos);

        List<Producto> result = productoService.getProductosDisponibles();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateStock() {
        Producto producto = createProducto(1L, "P1", 100.0);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.updateStock(1L, 10);

        assertEquals(10, result.getStock());
    }

    @Test
    void testUpdateStockWithNegativeQuantity() {
        // No necesitamos configurar el mock del repository porque
        // la validación ocurre antes de la búsqueda
        assertThrows(IllegalArgumentException.class, () -> {
            productoService.updateStock(1L, -1);
        });

        // Verificar que el repository nunca fue llamado
        verify(productoRepository, never()).findById(anyLong());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.getProductoById(99L);
        });
    }
}
