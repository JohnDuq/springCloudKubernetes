package com.jduquer.springcloudkube.usuarios.controller;

import com.jduquer.springcloudkube.usuarios.model.entity.Usuario;
import com.jduquer.springcloudkube.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> optUsuario = usuarioService.porId(id);
        return optUsuario.isPresent() ? ResponseEntity.ok(optUsuario.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> optUsuario = usuarioService.porId(id);
        if (optUsuario.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> optUsuario = usuarioService.porId(id);
        if (optUsuario.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
