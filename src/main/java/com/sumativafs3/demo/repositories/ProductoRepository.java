// ProductoRepository.java
package com.sumativafs3.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativafs3.demo.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockGreaterThan(int cantidad);
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByCompraId(Long compraId);
}