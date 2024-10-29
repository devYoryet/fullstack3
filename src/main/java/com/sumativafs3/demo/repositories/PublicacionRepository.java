package com.sumativafs3.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativafs3.demo.models.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}
