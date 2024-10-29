// ComentarioService.java
package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Comentario;
import com.sumativafs3.demo.models.Publicacion;
import com.sumativafs3.demo.repositories.ComentarioRepository;
import com.sumativafs3.demo.repositories.PublicacionRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    public List<Comentario> getComentariosByPublicacionId(Long publicacionId) {
        if (!publicacionRepository.existsById(publicacionId)) {
            throw new ResourceNotFoundException("Publicacion", "id", publicacionId);
        }
        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        if (comentarios.isEmpty()) {
            throw new ResourceNotFoundException("Comentarios", "publicacionId", publicacionId);
        }
        return comentarios;
    }

    public Comentario createComentario(Comentario comentario) {
        if (!publicacionRepository.existsById(comentario.getPublicacion().getId())) {
            throw new ResourceNotFoundException("Publicacion", "id", comentario.getPublicacion().getId());
        }
        return comentarioRepository.save(comentario);
    }

    public Comentario getComentarioById(Long publicacionId, Long comentarioId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));

        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new ResourceNotFoundException("Comentario", "publicacionId", publicacionId);
        }

        return comentario;
    }

    public Comentario updateComentario(Long publicacionId, Long comentarioId, Comentario comentarioDetails) {
        Comentario comentario = getComentarioById(publicacionId, comentarioId); // Esto ya maneja las excepciones
        comentario.setAutor(comentarioDetails.getAutor());
        comentario.setMensaje(comentarioDetails.getMensaje());
        comentario.setNota(comentarioDetails.getNota());
        return comentarioRepository.save(comentario);
    }

    public void deleteComentario(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comentario", "id", id);
        }
        comentarioRepository.deleteById(id);
    }
}
