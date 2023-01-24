package com.jduquer.springcloudkube.cursos.controller;

import com.jduquer.springcloudkube.cursos.model.Usuario;
import com.jduquer.springcloudkube.cursos.model.entity.Curso;
import com.jduquer.springcloudkube.cursos.service.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> optCurso = cursoService.porIdConUsuarios(id);
        return optCurso.isPresent() ? ResponseEntity.ok(optCurso.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody @Valid Curso curso, BindingResult bindingResult) {
        return bindingResult.hasErrors() ? administrarErrores(bindingResult) :
                ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid Curso curso, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return administrarErrores(bindingResult);
        } else {
            Optional<Curso> optCurso = cursoService.porId(id);
            if (optCurso.isPresent()) {
                curso.setId(id);
                return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
            } else {
                return ResponseEntity.notFound().build();
            }
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

    @PutMapping("/asignar-usuario/{id}")
    public ResponseEntity<?> asignarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> optionalUsuario;
        try {
            optionalUsuario = cursoService.asignarUsuario(id, usuario);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe " +
                    "el usuario por el id o error en la comunicación: " + e.getMessage()));
        }
        if (optionalUsuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{id}")
    public ResponseEntity<?> crearUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> optionalUsuario;
        try {
            optionalUsuario = cursoService.crearUsuario(id, usuario);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo " +
                    "crear el usuario por el id o error en la comunicación: " + e.getMessage()));
        }
        if (optionalUsuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> optionalUsuario;
        try {
            optionalUsuario = cursoService.eliminarUsuario(id, usuario);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe " +
                    "el usuario por el id o error en la comunicación: " + e.getMessage()));
        }
        if (optionalUsuario.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> administrarErrores(BindingResult bindingResult) {
        Map<String, String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

}
