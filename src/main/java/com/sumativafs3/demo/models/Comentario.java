package com.sumativafs3.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autor;
    private String mensaje;
    private Integer nota;

    // Relación ManyToOne con Publicacion. Usamos JsonBackReference para prevenir la recursividad en JSON.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id")
    @JsonBackReference
    private Publicacion publicacion;

    public Comentario() {
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }
}
