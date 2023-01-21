package com.jduquer.springcloudkube.usuarios.repository;

import com.jduquer.springcloudkube.usuarios.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
