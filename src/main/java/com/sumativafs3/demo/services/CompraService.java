// CompraService.java
package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.models.DetalleCompra;
import com.sumativafs3.demo.repositories.CompraRepository;
import com.sumativafs3.demo.repositories.DetalleCompraRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    public List<Compra> getAllCompras() {
        List<Compra> compras = compraRepository.findAll();
        if (compras.isEmpty()) {
            throw new ResourceNotFoundException("Compras", "lista", "vacía");
        }
        return compras;
    }

    public Compra getCompraById(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra", "id", id));
    }

    @Transactional
    public Compra createCompra(Compra compra) {
        // Guardar la compra primero
        compra.setTotal(0.0); // Inicializar total
        Compra savedCompra = compraRepository.save(compra);
        // Procesar cada detalle
        if (compra.getDetalles() != null) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                detalle.setCompra(savedCompra);
                detalle.setSubtotal(detalle.getPrecioUnitario() * detalle.getCantidad());
                detalleCompraRepository.save(detalle);
            }
        }

        // Recalcular el total
        double total = savedCompra.getDetalles().stream()
                .mapToDouble(DetalleCompra::getSubtotal)
                .sum();
        savedCompra.setTotal(total);

        return compraRepository.save(savedCompra);
    }

    @Transactional
    public Compra updateCompra(Long id, Compra compraDetails) {
        Compra compra = getCompraById(id);

        // Actualizar campos básicos
        compra.setEstadoCompra(compraDetails.getEstadoCompra());
        compra.setFechaCompra(compraDetails.getFechaCompra());

        // Actualizar detalles si se proporcionan
        if (compraDetails.getDetalles() != null) {
            // Eliminar detalles antiguos
            detalleCompraRepository.deleteAll(compra.getDetalles());

            // Agregar nuevos detalles
            compra.getDetalles().clear();
            for (DetalleCompra detalle : compraDetails.getDetalles()) {
                detalle.setCompra(compra);
                detalle.setSubtotal(detalle.getPrecioUnitario() * detalle.getCantidad());
                compra.addDetalle(detalle);
            }
        }

        return compraRepository.save(compra);
    }

    @Transactional
    public void deleteCompra(Long id) {
        Compra compra = getCompraById(id);
        // Los detalles se eliminarán automáticamente por la cascada
        compraRepository.delete(compra);
    }

    public List<Compra> getComprasByUsuarioId(Long usuarioId) {
        return compraRepository.findByUsuarioId(usuarioId);
    }

    public List<Compra> getComprasByEstado(String estado) {
        return compraRepository.findByEstadoCompra(estado);
    }
}
