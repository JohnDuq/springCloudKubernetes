package com.jduquer.springcloudkube.cursos.controller;

import com.jduquer.springcloudkube.cursos.model.entity.Curso;
import com.jduquer.springcloudkube.cursos.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> optCurso = cursoService.porId(id);
        return optCurso.isPresent() ? ResponseEntity.ok(optCurso.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Curso curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Curso curso) {
        Optional<Curso> optCurso = cursoService.porId(id);
        if (optCurso.isPresent()) {
            curso.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> optCurso = cursoService.porId(id);
        if (optCurso.isPresent()) {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
