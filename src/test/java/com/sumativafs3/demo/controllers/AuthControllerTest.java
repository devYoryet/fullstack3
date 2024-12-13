package com.sumativafs3.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.services.UsuarioService;
import com.sumativafs3.demo.response.LoginRequest;
import com.sumativafs3.demo.response.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

class AuthControllerTest {

    private AuthController authController;

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Crear el controller con las dependencias mockeadas
        authController = new AuthController(authenticationManager, usuarioService, rolRepository);
        // Inyectar el passwordEncoder manualmente
        authController.passwordEncoder = passwordEncoder;
    }

    @Test
    void testLogin_Success() {
        // Preparar datos de prueba
        String email = "test@example.com";
        String password = "Password123!";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Configurar comportamiento de los mocks
        when(usuarioService.findByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.matches(password, usuario.getPassword())).thenReturn(true);

        // Ejecutar el test
        ResponseEntity<Usuario> response = authController.login(loginRequest);

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testRegistro_Success() {
        // Preparar datos de prueba
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");

        Rol rolUser = new Rol();
        rolUser.setNombre("ROLE_USER");

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword("encodedPassword");
        usuario.setRol(rolUser);

        // Configurar comportamiento de los mocks
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(rolUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar el test
        ResponseEntity<?> response = authController.registro(request);

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testRegistroAdmin_Success() {
        // Preparar datos de prueba
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Admin User");
        request.setEmail("admin@example.com");
        request.setPassword("Admin123!");

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ROLE_ADMIN");

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword("encodedPassword");
        usuario.setRol(rolAdmin);

        // Configurar comportamiento de los mocks
        when(rolRepository.findByNombre("ROLE_ADMIN")).thenReturn(Optional.of(rolAdmin));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar el test
        ResponseEntity<?> response = authController.registroAdmin(request);

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Usuario);
        Usuario responseBody = (Usuario) response.getBody();
        assertEquals("Admin User", responseBody.getNombre());
        assertEquals("admin@example.com", responseBody.getEmail());
        assertEquals(rolAdmin, responseBody.getRol());
    }
}