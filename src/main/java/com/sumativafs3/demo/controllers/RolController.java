package com.sumativafs3.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.services.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    // Endpoint público para crear roles (solo para configuración inicial)
    @PostMapping("/crear")
    @Secured("ROLE_ADMIN")  // Solo admin puede ver todos los roles

    public ResponseEntity<?> createRol(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.createRol(rol));
    }

    // Consultar todos los roles
    @GetMapping
    @Secured("ROLE_ADMIN")  // Solo admin puede ver todos los roles
    public ResponseEntity<List<Rol>> getAllRoles() {
        return ResponseEntity.ok(rolService.getAllRoles());
    }

    // Consultar rol por ID
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Rol> getRolById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.getRolById(id));
    }

    // Consultar rol por nombre
    @GetMapping("/nombre/{nombre}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Rol> getRolByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(rolService.getRolByNombre(nombre));
    }
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteRol(@PathVariable Long id) {
        rolService.deleteRol(id);
        return ResponseEntity.ok().body("Rol eliminado exitosamente");
    }
}