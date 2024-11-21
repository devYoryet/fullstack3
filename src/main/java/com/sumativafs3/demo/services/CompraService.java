// CompraService.java
package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.repositories.CompraRepository;

@Service
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;
    
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
    
    public Compra createCompra(Compra compra) {
        compra.calcularTotal();
        return compraRepository.save(compra);
    }
    
    public Compra updateCompra(Long id, Compra compraDetails) {
        Compra compra = getCompraById(id);
        compra.setEstadoCompra(compraDetails.getEstadoCompra());
        compra.setProductos(compraDetails.getProductos());
        compra.calcularTotal();
        return compraRepository.save(compra);
    }
    
    public void deleteCompra(Long id) {
        Compra compra = getCompraById(id);
        compraRepository.delete(compra);
    }
}
