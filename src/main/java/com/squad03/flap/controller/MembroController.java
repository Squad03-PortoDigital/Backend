package com.squad03.flap.controller;

import com.squad03.flap.DTO.MembroCreateDTO;
import com.squad03.flap.DTO.MembroDTO;
import com.squad03.flap.service.MembroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membros")
@CrossOrigin(origins = "*")
@Tag(name = "Membros", description = "Gerenciamento de membros")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @PostMapping
    public ResponseEntity<MembroDTO> criarMembro(@Valid @RequestBody MembroCreateDTO createDTO) {
        try {
            MembroDTO membroDTO = membroService.criarMembro(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(membroDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<MembroDTO>> buscarTodosMembros() {
        List<MembroDTO> membros = membroService.buscarTodosMembros();
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroDTO> buscarMembroPorId(@PathVariable Long id) {
        try {
            MembroDTO membroDTO = membroService.buscarMembroPorId(id);
            return ResponseEntity.ok(membroDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MembroDTO>> buscarMembrosPorUsuario(@PathVariable Long usuarioId) {
        List<MembroDTO> membros = membroService.buscarMembrosPorUsuario(usuarioId);
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<MembroDTO>> buscarMembrosPorTarefa(@PathVariable Long tarefaId) {
        List<MembroDTO> membros = membroService.buscarMembrosPorTarefa(tarefaId);
        return ResponseEntity.ok(membros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembroDTO> atualizarMembro(@PathVariable Long id,
                                                     @Valid @RequestBody MembroCreateDTO updateDTO) {
        try {
            MembroDTO membroDTO = membroService.atualizarMembro(id, updateDTO);
            return ResponseEntity.ok(membroDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMembro(@PathVariable Long id) {
        try {
            membroService.deletarMembro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/tarefa/{tarefaId}")
    public ResponseEntity<Void> deletarMembrosPorTarefa(@PathVariable Long tarefaId) {
        membroService.deletarMembrosPorTarefa(tarefaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> deletarMembrosPorUsuario(@PathVariable Long usuarioId) {
        membroService.deletarMembrosPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}