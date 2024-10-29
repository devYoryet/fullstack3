// InitialDataLoader.java
package com.sumativafs3.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
     private void updateExistingRoles() {
        rolRepository.findAll().forEach(rol -> {
            if (!rol.getNombre().startsWith("ROLE_")) {
                rol.setNombre("ROLE_" + rol.getNombre());
                rolRepository.save(rol);
            }
        });
    }
    @Override
    public void run(String... args) {
        updateExistingRoles();

        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            Rol adminRol = new Rol();
            adminRol.setNombre("ADMIN");
            rolRepository.save(adminRol);

            Rol userRol = new Rol();
            userRol.setNombre("USER");
            rolRepository.save(userRol);

            // Crear usuario admin por defecto
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(adminRol);
            usuarioRepository.save(admin);

            // Crear usuario normal por defecto
            Usuario user = new Usuario();
            user.setNombre("User");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRol(userRol);
            usuarioRepository.save(user);
        }
    }
}
