package com.squad03.flap.controller;

import com.squad03.flap.DTO.ComentarioRequestDTO;
import com.squad03.flap.DTO.ComentarioResponseDTO;
import com.squad03.flap.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> criarComentario(@Valid @RequestBody ComentarioRequestDTO dto) {
        try {
            ComentarioResponseDTO comentario = comentarioService.criarComentario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listarTodos() {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarTodos();
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            ComentarioResponseDTO comentario = comentarioService.buscarPorId(id);
            return ResponseEntity.ok(comentario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarPorTarefa(@PathVariable Long tarefaId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.buscarPorTarefa(tarefaId);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/usuario/{usuarioId}/tarefa/{tarefaId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarPorUsuarioETarefa(
            @PathVariable Long usuarioId,
            @PathVariable Long tarefaId) {
        List<ComentarioResponseDTO> comentarios = comentarioService.buscarPorUsuarioETarefa(usuarioId, tarefaId);
        return ResponseEntity.ok(comentarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> atualizarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioRequestDTO dto) {
        try {
            ComentarioResponseDTO comentario = comentarioService.atualizarComentario(id, dto);
            return ResponseEntity.ok(comentario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> deletarComentario(
            @PathVariable Long id,
            @PathVariable Long usuarioId) {
        try {
            comentarioService.deletarComentario(id, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}