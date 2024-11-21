package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;
import com.sumativafs3.demo.utils.PasswordValidator;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    public Usuario registrarUsuario(Usuario usuario) {
        // Validar que el email no exista
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar la contraseña
        PasswordValidator.validatePassword(usuario.getPassword());

        // Buscar el rol por nombre
        String nombreRol = usuario.getRol().getNombre();
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", nombreRol));

        // Asignar el rol encontrado
        usuario.setRol(rol);

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar el usuario
        return usuarioRepository.save(usuario);
    }


    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Usuario usuario = getUsuarioById(id);
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        
        if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
            // Validar la nueva contraseña
            PasswordValidator.validatePassword(usuarioDetails.getPassword());
            usuario.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
        }
        
        if (usuarioDetails.getRol() != null) {
            usuario.setRol(usuarioDetails.getRol());
        }
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        Usuario usuario = getUsuarioById(id);
        usuarioRepository.delete(usuario);
    }
}