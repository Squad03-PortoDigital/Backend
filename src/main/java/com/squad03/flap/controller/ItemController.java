package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoItem;
import com.squad03.flap.DTO.BuscaItem;
import com.squad03.flap.DTO.CadastroItem;
import com.squad03.flap.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<BuscaItem> criarItem(@RequestBody @Valid CadastroItem cadastroItem) {
        try {
            BuscaItem item = itemService.cadastrarItem(cadastroItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BuscaItem>> listarItens() {
        List<BuscaItem> itens = itemService.listarItens();
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaItem> buscarItemPorId(@PathVariable Long id) {
        Optional<BuscaItem> item = itemService.buscarItemPorId(id);
        return item.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<BuscaItem>> listarItensPorTarefa(@PathVariable Long tarefaId) {
        List<BuscaItem> itens = itemService.listarItensPorTarefa(tarefaId);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/checklist/{checklistId}")
    public ResponseEntity<List<BuscaItem>> listarItensPorChecklist(@PathVariable Long checklistId) {
        List<BuscaItem> itens = itemService.listarItensPorChecklist(checklistId);
        return ResponseEntity.ok(itens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaItem> atualizarItem(@PathVariable Long id, 
                                                   @RequestBody @Valid AtualizacaoItem atualizacaoItem) {
        Optional<BuscaItem> itemAtualizado = itemService.atualizarItem(id, atualizacaoItem);
        return itemAtualizado.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<BuscaItem> alternarStatusItem(@PathVariable Long id) {
        Optional<BuscaItem> itemAtualizado = itemService.alternarStatusItem(id);
        return itemAtualizado.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable Long id) {
        boolean deletado = itemService.deletarItem(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}