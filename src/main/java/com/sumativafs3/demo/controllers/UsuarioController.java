package com.sumativafs3.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@Secured("ROLE_ADMIN")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }
    @GetMapping("/{id}/rol")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> getRolByUsuarioId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getRolByUsuarioId(id));
    }
    @GetMapping("/rol")
@Secured("ROLE_ADMIN")
public ResponseEntity<String> getRolByUsuarioEmail(@RequestParam String email) {
    return ResponseEntity.ok(usuarioService.getRolByUsuarioEmail(email));
}
}