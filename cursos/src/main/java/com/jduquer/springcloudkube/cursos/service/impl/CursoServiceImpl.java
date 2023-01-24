package com.jduquer.springcloudkube.cursos.service.impl;

import com.jduquer.springcloudkube.cursos.client.UsuarioClientRest;
import com.jduquer.springcloudkube.cursos.model.Usuario;
import com.jduquer.springcloudkube.cursos.model.entity.Curso;
import com.jduquer.springcloudkube.cursos.model.entity.CursoUsuario;
import com.jduquer.springcloudkube.cursos.repository.CursoRepository;
import com.jduquer.springcloudkube.cursos.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioClientRest usuarioClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> asignarUsuario(Long cursoId, Usuario usuario) {
        Optional<Curso> oCurso = cursoRepository.findById(cursoId);
        if(oCurso.isPresent()){
            Curso curso = oCurso.get();
            Usuario usuarioObtenido = usuarioClientRest.detalle(usuario.getId());
            CursoUsuario cursoUsuario = CursoUsuario.builder().usuarioId(usuarioObtenido.getId()).build();
            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioObtenido);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Long cursoId, Usuario usuario) {
        Optional<Curso> oCurso = cursoRepository.findById(cursoId);
        if(oCurso.isPresent()){
            Curso curso = oCurso.get();
            Usuario usuarioObtenido = usuarioClientRest.crear(usuario);
            CursoUsuario cursoUsuario = CursoUsuario.builder().usuarioId(usuarioObtenido.getId()).build();
            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioObtenido);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> eliminarUsuario(Long cursoId, Usuario usuario) {
        Optional<Curso> oCurso = cursoRepository.findById(cursoId);
        if(oCurso.isPresent()){
            Curso curso = oCurso.get();
            Usuario usuarioObtenido = usuarioClientRest.detalle(usuario.getId());
            CursoUsuario cursoUsuario = CursoUsuario.builder().usuarioId(usuarioObtenido.getId()).build();
            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioObtenido);
        }
        return Optional.empty();
    }

}
