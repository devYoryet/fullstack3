package com.sumativafs3.demo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private String estadoCompra; // "PENDIENTE", "COMPLETADA", "CANCELADA"

    @Column(nullable = false)
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference // Relación gestionada, evita ciclos
    private List<DetalleCompra> detalles = new ArrayList<>();

    public Compra() {
        this.fechaCompra = LocalDateTime.now();
        this.total = 0.0;
        this.estadoCompra = "PENDIENTE";
    }

    // Métodos Helper
    public void addDetalle(DetalleCompra detalle) {
        detalles.add(detalle);
        detalle.setCompra(this);
        calcularTotal();
    }

    public void removeDetalle(DetalleCompra detalle) {
        detalles.remove(detalle);
        detalle.setCompra(null);
        calcularTotal();
    }

    private void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetalleCompra::getSubtotal)
                .sum();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

}