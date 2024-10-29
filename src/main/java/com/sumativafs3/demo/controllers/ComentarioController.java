// ComentarioController.java
package com.sumativafs3.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumativafs3.demo.models.Comentario;
import com.sumativafs3.demo.models.Publicacion;
import com.sumativafs3.demo.services.ComentarioService;

@RestController
@RequestMapping("/api/publicaciones/{publicacionId}/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> getComentariosByPublicacionId(@PathVariable Long publicacionId) {
        return comentarioService.getComentariosByPublicacionId(publicacionId);
    }

    @GetMapping("/{comentarioId}")
    public Comentario getComentarioById(@PathVariable Long publicacionId, @PathVariable Long comentarioId) {
        return comentarioService.getComentarioById(publicacionId, comentarioId);
    }

    @PostMapping
    public Comentario createComentario(@PathVariable Long publicacionId, @RequestBody Comentario comentario) {
        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);
        comentario.setPublicacion(publicacion);
        return comentarioService.createComentario(comentario);
    }

    @PutMapping("/{comentarioId}")
    public Comentario updateComentario(@PathVariable Long publicacionId, 
                                     @PathVariable Long comentarioId, 
                                     @RequestBody Comentario comentarioDetails) {
        return comentarioService.updateComentario(publicacionId, comentarioId, comentarioDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteComentario(@PathVariable Long id) {
        comentarioService.deleteComentario(id);
    }
}