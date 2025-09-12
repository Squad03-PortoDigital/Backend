package com.squad03.flap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.squad03.flap.DTO.*;
import com.squad03.flap.model.Agente;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.service.TarefaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*")
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas do sistema Kanban")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista com todas as tarefas cadastradas no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    public ResponseEntity<List<TarefaDTO>> getAllTarefas() {
        List<TarefaDTO> tarefas = tarefaService.getAllTarefas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica baseada no ID fornecido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TarefaDTO> getTarefaById(
            @Parameter(description = "ID da tarefa") @PathVariable Long id) {
        return tarefaService.getTarefaById(id)
                .map(tarefa -> ResponseEntity.ok(tarefa))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TarefaDTO> criarTarefa(
            @Parameter(description = "Dados para criar a tarefa") @RequestBody CriarTarefaDTO criarTarefaDTO,
            @Parameter(description = "ID do agente responsável") @RequestParam Long agenteId) {
        // Em uma implementação real, você buscaria o agente pelo ID
        // Aqui assumindo que você tem um serviço para buscar agente
        Agente agente = new Agente();
        // O Agente não tem setter para id, pois é gerado automaticamente
        // Você precisaria buscar o agente do banco de dados pelo ID
        
        TarefaDTO novaTarefa = tarefaService.criarTarefa(criarTarefaDTO, agente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TarefaDTO> atualizarTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "Dados para atualizar") @RequestBody AtualizarTarefaDTO atualizarDTO) {
        return tarefaService.atualizarTarefa(id, atualizarDTO)
                .map(tarefa -> ResponseEntity.ok(tarefa))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/mover")
    @Operation(summary = "Mover tarefa no Kanban", description = "Move uma tarefa entre colunas ou altera sua posição")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa movida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TarefaDTO> moverTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "Nova posição e/ou status") @RequestBody MoverTarefaDTO moverDTO) {
        return tarefaService.moverTarefa(id, moverDTO)
                .map(tarefa -> ResponseEntity.ok(tarefa))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> deletarTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id) {
        if (tarefaService.deletarTarefa(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/agente/{agenteId}")
    @Operation(summary = "Listar tarefas por agente", description = "Retorna todas as tarefas de um agente específico")
    public ResponseEntity<List<TarefaDTO>> getTarefasPorAgente(
            @Parameter(description = "ID do agente") @PathVariable Long agenteId) {
        // Em uma implementação real, você buscaria o agente pelo ID
        Agente agente = new Agente();
        // Aqui você precisaria buscar o agente do banco de dados
        
        List<TarefaDTO> tarefas = tarefaService.getTarefasPorAgente(agente);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar tarefas por status", description = "Retorna todas as tarefas com um status específico")
    public ResponseEntity<List<TarefaDTO>> getTarefasPorStatus(
            @Parameter(description = "Status das tarefas") @PathVariable StatusTarefa status) {
        List<TarefaDTO> tarefas = tarefaService.getTarefasPorStatus(status);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/prioridade/{prioridade}")
    @Operation(summary = "Listar tarefas por prioridade", description = "Retorna todas as tarefas com uma prioridade específica")
    public ResponseEntity<List<TarefaDTO>> getTarefasPorPrioridade(
            @Parameter(description = "Prioridade das tarefas") @PathVariable PrioridadeTarefa prioridade) {
        List<TarefaDTO> tarefas = tarefaService.getTarefasPorPrioridade(prioridade);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar tarefas", description = "Busca tarefas por título ou descrição")
    public ResponseEntity<List<TarefaDTO>> buscarTarefas(
            @Parameter(description = "Título para buscar") @RequestParam(required = false) String titulo,
            @Parameter(description = "Descrição para buscar") @RequestParam(required = false) String descricao) {
        
        if (titulo != null && !titulo.trim().isEmpty()) {
            return ResponseEntity.ok(tarefaService.buscarTarefasPorTitulo(titulo));
        }
        
        if (descricao != null && !descricao.trim().isEmpty()) {
            return ResponseEntity.ok(tarefaService.buscarTarefasPorDescricao(descricao));
        }
        
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/atrasadas")
    @Operation(summary = "Listar tarefas atrasadas", description = "Retorna todas as tarefas que passaram do prazo de entrega")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas atrasadas")
    public ResponseEntity<List<TarefaDTO>> getTarefasAtrasadas() {
        List<TarefaDTO> tarefas = tarefaService.getTarefasAtrasadas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Listar tarefas por tag", description = "Retorna todas as tarefas que possuem uma tag específica")
    public ResponseEntity<List<TarefaDTO>> getTarefasPorTag(
            @Parameter(description = "Tag para filtrar") @PathVariable String tag) {
        List<TarefaDTO> tarefas = tarefaService.getTarefasPorTag(tag);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar tarefas por período", description = "Retorna tarefas com prazo de entrega em um período específico")
    public ResponseEntity<List<TarefaDTO>> getTarefasPorPeriodo(
            @Parameter(description = "Data início do período") @RequestParam LocalDateTime dataInicio,
            @Parameter(description = "Data fim do período") @RequestParam LocalDateTime dataFim) {
        List<TarefaDTO> tarefas = tarefaService.getTarefasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/contador")
    @Operation(summary = "Contar tarefas por agente e status", description = "Retorna a quantidade de tarefas de um agente em um status específico")
    public ResponseEntity<Long> contarTarefasPorAgenteEStatus(
            @Parameter(description = "ID do agente") @RequestParam Long agenteId,
            @Parameter(description = "Status das tarefas") @RequestParam StatusTarefa status) {
        
        Agente agente = new Agente();
        // Aqui você precisaria buscar o agente do banco de dados pelo ID
        
        Long count = tarefaService.contarTarefasPorAgenteEStatus(agente, status);
        return ResponseEntity.ok(count);
    }
}
