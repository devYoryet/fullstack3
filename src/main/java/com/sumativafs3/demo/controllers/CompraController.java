// CompraController.java
package com.sumativafs3.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.services.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<Compra> getAllCompras() {
        return compraService.getAllCompras();
    }
    
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Compra createCompra(@RequestBody Compra compra) {
        return compraService.createCompra(compra);
    }
    
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Compra getCompraById(@PathVariable Long id) {
        return compraService.getCompraById(id);
    }
    
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public Compra updateCompra(@PathVariable Long id, @RequestBody Compra compraDetails) {
        return compraService.updateCompra(id, compraDetails);
    }
    
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public void deleteCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
    }
}