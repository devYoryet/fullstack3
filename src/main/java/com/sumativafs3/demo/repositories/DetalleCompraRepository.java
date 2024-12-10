// src/main/java/com/sumativafs3/demo/repositories/DetalleCompraRepository.java
package com.sumativafs3.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sumativafs3.demo.models.DetalleCompra;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
}