package com.jduquer.springcloudkube.cursos.repository;

import com.jduquer.springcloudkube.cursos.model.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
