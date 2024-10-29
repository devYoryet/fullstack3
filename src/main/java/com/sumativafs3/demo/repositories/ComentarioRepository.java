package com.sumativafs3.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativafs3.demo.models.Comentario;
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacionId(Long publicacionId);

}
