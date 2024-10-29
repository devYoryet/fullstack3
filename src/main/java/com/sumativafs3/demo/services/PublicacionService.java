package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Publicacion;
import com.sumativafs3.demo.repositories.PublicacionRepository;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    public List<Publicacion> getAllPublicaciones() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        if (publicaciones.isEmpty()) {
            throw new ResourceNotFoundException("Publicaciones", "lista", "vacía");
        }
        return publicaciones;
    }

    public Publicacion getPublicacionById(Long id) {
        return publicacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));
    }

    public Publicacion createPublicacion(Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    public Publicacion updatePublicacion(Long id, Publicacion publicacionDetails) {
        Publicacion publicacion = getPublicacionById(id); // Esto ya lanza la excepción si no existe
        publicacion.setTitulo(publicacionDetails.getTitulo());
        publicacion.setContenido(publicacionDetails.getContenido());
        return publicacionRepository.save(publicacion);
    }

    public void deletePublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id); // Esto ya lanza la excepción si no existe
        publicacionRepository.delete(publicacion);
    }
}
