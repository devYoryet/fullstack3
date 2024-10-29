package com.sumativafs3.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumativafs3.demo.exceptions.ResourceNotFoundException;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;


@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Rol> getAllRoles() {
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("Roles", "lista", "vacía");
        }
        return roles;
    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));
    }

    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
            .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", nombre));
    }

    public Rol createRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Transactional
    public void deleteRol(Long id) {
        Rol rol = rolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));

        // Verificar si hay usuarios usando este rol
        if (usuarioRepository.existsByRolId(id)) {
            throw new RuntimeException("No se puede eliminar el rol porque está asignado a uno o más usuarios");
        }

        rolRepository.delete(rol);
    }
}