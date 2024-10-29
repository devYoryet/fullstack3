// UsuarioRepository.java
package com.sumativafs3.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativafs3.demo.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
      // Agregar este método
      boolean existsByRolId(Long rolId);
}
