// ProductoController.java
package com.sumativafs3.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.sumativafs3.demo.models.Producto;
import com.sumativafs3.demo.services.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.getProductoById(id);
        return ResponseEntity.ok(producto);
    }

    // Crear un nuevo producto
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Producto> updateProducto(
            @PathVariable Long id, 
            @RequestBody Producto productoDetails) {
        Producto updatedProducto = productoService.updateProducto(id, productoDetails);
        return ResponseEntity.ok(updatedProducto);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok().build();
    }

    // Obtener productos con stock disponible
    @GetMapping("/disponibles")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<Producto>> getProductosDisponibles() {
        List<Producto> productos = productoService.getProductosDisponibles();
        return ResponseEntity.ok(productos);
    }

    // Actualizar stock de un producto
    @PutMapping("/{id}/stock")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Producto> updateStock(
            @PathVariable Long id, 
            @RequestParam int cantidad) {
        Producto producto = productoService.updateStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }
}