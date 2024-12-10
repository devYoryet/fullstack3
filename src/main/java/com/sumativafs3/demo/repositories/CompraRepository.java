package com.sumativafs3.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sumativafs3.demo.models.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioId(Long usuarioId);

    List<Compra> findByEstadoCompra(String estado);
}