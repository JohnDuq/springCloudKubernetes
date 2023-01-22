package com.jduquer.springcloudkube.usuarios.controller;

import com.jduquer.springcloudkube.usuarios.model.entity.Usuario;
import com.jduquer.springcloudkube.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> crear(@RequestBody @Valid Usuario usuario, BindingResult bindingResult) {
        return bindingResult.hasErrors() ? administrarErrores(bindingResult) :
                ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid Usuario usuario, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return administrarErrores(bindingResult);
        } else {
            Optional<Usuario> optUsuario = usuarioService.porId(id);
            if (optUsuario.isPresent()) {
                usuario.setId(id);
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
            } else {
                return ResponseEntity.notFound().build();
            }
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

    private static ResponseEntity<Map<String, String>> administrarErrores(BindingResult bindingResult) {
        Map<String, String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

}
