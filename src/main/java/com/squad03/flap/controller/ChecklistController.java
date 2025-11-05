package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoChecklist;
import com.squad03.flap.DTO.BuscaChecklist;
import com.squad03.flap.DTO.CadastroChecklist;
import com.squad03.flap.DTO.ChecklistResponseDTO;
import com.squad03.flap.service.ChecklistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    private final ChecklistService checklistService;

    @Autowired
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PostMapping
    public ResponseEntity<BuscaChecklist> criarChecklist(@RequestBody @Valid CadastroChecklist cadastroChecklist) {
        try {
            BuscaChecklist checklist = checklistService.cadastrarChecklist(cadastroChecklist);
            return ResponseEntity.status(HttpStatus.CREATED).body(checklist);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BuscaChecklist>> listarChecklists() {
        List<BuscaChecklist> checklists = checklistService.listarChecklists();
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaChecklist> buscarChecklistPorId(@PathVariable Long id) {
        Optional<BuscaChecklist> checklist = checklistService.buscarChecklistPorId(id);
        return checklist.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/com-itens")
    public ResponseEntity<BuscaChecklist> buscarChecklistComItens(@PathVariable Long id) {
        Optional<BuscaChecklist> checklist = checklistService.buscarChecklistComItens(id);
        return checklist.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<ChecklistResponseDTO>> listarChecklistsPorTarefaComItens(@PathVariable Long tarefaId) {
        List<ChecklistResponseDTO> checklists = checklistService.listarChecklistsPorTarefa(tarefaId);
        return ResponseEntity.ok(checklists);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BuscaChecklist> atualizarChecklist(@PathVariable Long id, 
                                                           @RequestBody @Valid AtualizacaoChecklist atualizacaoChecklist) {
        Optional<BuscaChecklist> checklistAtualizado = checklistService.atualizarChecklist(id, atualizacaoChecklist);
        return checklistAtualizado.map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarChecklist(@PathVariable Long id) {
        boolean deletado = checklistService.deletarChecklist(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}