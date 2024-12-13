package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Producto;
import com.sumativafs3.demo.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los productos
    public List<Producto> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("Productos", "lista", "vacía");
        }
        return productos;
    }

    // Obtener un producto por ID
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }

    // Crear un nuevo producto
    public Producto createProducto(Producto producto) {
        // Validaciones básicas
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = getProductoById(id);

        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setStock(productoDetails.getStock());
        producto.setCantidad(productoDetails.getCantidad());

        if (productoDetails.getImagen() != null) {
            producto.setImagen(productoDetails.getImagen());
        }

        return productoRepository.save(producto);
    }

    // Eliminar un producto
    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        productoRepository.delete(producto);
    }

    // Obtener productos con stock disponible
    public List<Producto> getProductosDisponibles() {
        return productoRepository.findByStockGreaterThan(0);
    }

    // Actualizar stock de un producto
    public Producto updateStock(Long id, int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        producto.setStock(cantidad);
        return productoRepository.save(producto);
    }

    // Actualizar stock después de una compra
    public void actualizarStockDespuesDeCompra(Long productoId, int cantidadComprada) {
        Producto producto = getProductoById(productoId);
        int nuevoStock = producto.getStock() - cantidadComprada;
        if (nuevoStock < 0) {
            throw new IllegalStateException("No hay suficiente stock disponible");
        }
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }
}