// AuthController.java (modificado)
package com.sumativafs3.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.response.LoginRequest;
import com.sumativafs3.demo.response.LoginResponse;
import com.sumativafs3.demo.response.RegisterRequest;
import com.sumativafs3.demo.services.UsuarioService;
import com.sumativafs3.demo.utils.PasswordValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, 
                         UsuarioService usuarioService,
                         RolRepository rolRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new LoginResponse("Usuario autenticado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new LoginResponse("Error en la autenticación: Credenciales inválidas"));
        }
    }

    // AuthController.java (modificado para manejar roles)
@PostMapping("/registro")
public ResponseEntity<?> registro(@RequestBody RegisterRequest registerRequest) {
    try {
        // Validaciones básicas
        if (registerRequest.getNombre() == null || registerRequest.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new LoginResponse("El nombre es obligatorio"));
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new LoginResponse("El email es obligatorio"));
        }
        if (!registerRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(new LoginResponse("El email no es válido"));
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new LoginResponse("La contraseña es obligatoria"));
        }

        // Validar contraseña
        try {
            PasswordValidator.validatePassword(registerRequest.getPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new LoginResponse(e.getMessage()));
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombre());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(registerRequest.getPassword());

        // Asignar rol USER por defecto
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Error: Rol de usuario no encontrado."));
        usuario.setRol(rolUser);

        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(new LoginResponse("Error al registrar el usuario: " + e.getMessage()));
    }
}
// AuthController.java (agregar este método)
@PostMapping("/registro/admin")
@Secured("ROLE_ADMIN")
public ResponseEntity<?> registroAdmin(@RequestBody RegisterRequest registerRequest) {
    try {
        // Validaciones...
        
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombre());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(registerRequest.getPassword());

        // Asignar rol ADMIN
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
            .orElseThrow(() -> new RuntimeException("Error: Rol de administrador no encontrado."));
        usuario.setRol(rolAdmin);

        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(new LoginResponse("Error al registrar el administrador: " + e.getMessage()));
    }
}
}