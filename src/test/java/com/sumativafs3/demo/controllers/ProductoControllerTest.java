package com.sumativafs3.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.sumativafs3.demo.controllers.ProductoController;
import com.sumativafs3.demo.models.Producto;
import com.sumativafs3.demo.services.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {
    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    void testGetAllProductos() {
        // Given
        List<Producto> productos = Arrays.asList(
                createProducto(1L, "Producto 1", 100.0),
                createProducto(2L, "Producto 2", 200.0));
        when(productoService.getAllProductos()).thenReturn(productos);

        // When
        ResponseEntity<List<Producto>> response = productoController.getAllProductos();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testCreateProducto() {
        // Given
        Producto producto = createProducto(1L, "Nuevo Producto", 100.0);
        when(productoService.createProducto(any(Producto.class))).thenReturn(producto);

        // When
        ResponseEntity<Producto> response = productoController.createProducto(producto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nuevo Producto", response.getBody().getNombre());
    }

    @Test
    void testDeleteProducto() {
        // When
        ResponseEntity<?> response = productoController.deleteProducto(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productoService).deleteProducto(1L);
    }

    private Producto createProducto(Long id, String nombre, Double precio) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        return producto;
    }

    @Test
    void testGetProductosDisponibles() {
        List<Producto> productos = Arrays.asList(
                createProducto(1L, "P1", 100.0),
                createProducto(2L, "P2", 200.0));
        when(productoService.getProductosDisponibles()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.getProductosDisponibles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdateStock() {
        Producto producto = createProducto(1L, "P1", 100.0);
        when(productoService.updateStock(1L, 10)).thenReturn(producto);

        ResponseEntity<Producto> response = productoController.updateStock(1L, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetProductoById() {
        // Given
        Long productId = 1L;
        Producto expectedProducto = createProducto(productId, "Producto Existente", 150.0);
        when(productoService.getProductoById(productId)).thenReturn(expectedProducto);

        // When
        ResponseEntity<Producto> response = productoController.getProductoById(productId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        assertEquals("Producto Existente", response.getBody().getNombre());
    }

    @Test
    void testUpdateProducto() {
        // Given
        Long productId = 1L;
        Producto originalProducto = createProducto(productId, "Producto Original", 100.0);
        Producto updatedDetails = createProducto(productId, "Producto Actualizado", 120.0);

        when(productoService.updateProducto(eq(productId), any(Producto.class))).thenReturn(updatedDetails);

        // When
        ResponseEntity<Producto> response = productoController.updateProducto(productId, updatedDetails);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto Actualizado", response.getBody().getNombre());
        assertEquals(Double.valueOf(120.0), response.getBody().getPrecio());

        verify(productoService).updateProducto(eq(productId), any(Producto.class)); // Verificar que el servicio fue
                                                                                    // llamado
    }

}