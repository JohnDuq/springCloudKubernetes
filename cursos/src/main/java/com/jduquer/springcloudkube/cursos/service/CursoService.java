package com.jduquer.springcloudkube.cursos.service;

import com.jduquer.springcloudkube.cursos.model.Usuario;
import com.jduquer.springcloudkube.cursos.model.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    Optional<Usuario> asignarUsuario(Long cursoId, Usuario usuario);
    Optional<Usuario> crearUsuario(Long cursoId, Usuario usuario);
    Optional<Usuario> eliminarUsuario(Long cursoId, Usuario usuario);

}
