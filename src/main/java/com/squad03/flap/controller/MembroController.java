package com.squad03.flap.controller;

import com.squad03.flap.DTO.MembroCreateDTO;
import com.squad03.flap.DTO.MembroDTO;
import com.squad03.flap.service.MembroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membros")
@CrossOrigin(origins = "*")
@Tag(name = "Membros", description = "Gerenciamento de membros da tarefa")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @PostMapping
    @PreAuthorize("hasAuthority('TAREFA_EDITAR_GERAL')")
    public ResponseEntity<MembroDTO> criarMembro(@Valid @RequestBody MembroCreateDTO createDTO) {
        try {
            MembroDTO membroSalvo = membroService.criarMembro(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(membroSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TAREFA_EDITAR_GERAL')")
    public ResponseEntity<Void> deletarMembro(@PathVariable Long id) {
        try {
            membroService.deletarMembro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<MembroDTO>> buscarMembrosPorTarefa(@PathVariable Long tarefaId) {
        List<MembroDTO> membros = membroService.buscarMembrosPorTarefa(tarefaId);
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MembroDTO>> buscarMembrosPorUsuario(@PathVariable Long usuarioId) {
        List<MembroDTO> membros = membroService.buscarMembrosPorUsuario(usuarioId);
        return ResponseEntity.ok(membros);
    }
}