// Compra.java
package com.sumativafs3.demo.models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "compras")
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime fechaCompra;
    private String estadoCompra; // "PENDIENTE", "COMPLETADA", "CANCELADA"
    private double totalCompra;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Producto> productos = new ArrayList<>();
    
    public Compra() {
        this.fechaCompra = LocalDateTime.now();
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    
    public String getEstadoCompra() { return estadoCompra; }
    public void setEstadoCompra(String estadoCompra) { this.estadoCompra = estadoCompra; }
    
    public double getTotalCompra() { return totalCompra; }
    public void setTotalCompra(double totalCompra) { this.totalCompra = totalCompra; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    
    // Método para calcular el total de la compra
    public void calcularTotal() {
        this.totalCompra = productos.stream()
                                  .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                                  .sum();
    }
}