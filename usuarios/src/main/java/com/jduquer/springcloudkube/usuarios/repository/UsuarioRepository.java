package com.jduquer.springcloudkube.usuarios.repository;

import com.jduquer.springcloudkube.usuarios.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
