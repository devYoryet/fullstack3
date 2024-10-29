// PublicacionController.java (modificado)
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

import com.sumativafs3.demo.models.Publicacion;
import com.sumativafs3.demo.services.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping
    @Secured("ROLE_ADMIN")  // Solo usuarios con rol ADMIN pueden acceder
    public List<Publicacion> getAllPublicaciones() {
        return publicacionService.getAllPublicaciones();
    }

    // Los demás métodos también pueden ser protegidos según necesites
    @PostMapping
    @Secured("ROLE_ADMIN")
    public Publicacion createPublicacion(@RequestBody Publicacion publicacion) {
        return publicacionService.createPublicacion(publicacion);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})  // Ambos roles pueden acceder
    public Publicacion getPublicacionById(@PathVariable Long id) {
        return publicacionService.getPublicacionById(id);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public Publicacion updatePublicacion(@PathVariable Long id, @RequestBody Publicacion publicacionDetails) {
        return publicacionService.updatePublicacion(id, publicacionDetails);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public void deletePublicacion(@PathVariable Long id) {
        publicacionService.deletePublicacion(id);
    }
}