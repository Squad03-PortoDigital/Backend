package com.squad03.flap.controller;

import com.squad03.flap.DTO.*;
import com.squad03.flap.exception.TarefaValidacaoException;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*")
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas do sistema Kanban")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;



    @GetMapping
    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista com todas as tarefas cadastradas no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    public ResponseEntity<List<BuscaTarefa>> getAllTarefas() {
        try {
            List<BuscaTarefa> tarefas = tarefaService.getAllTarefas();
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar tarefa com dados completos", description = "Retorna uma tarefa específica com todos os dados complexos (anexos, checklist, etc.)")
    public ResponseEntity<DetalheTarefa> getTarefaById(
            @Parameter(description = "ID da tarefa") @PathVariable Long id) {
        try {
            // Chama o novo método de detalhamento
            return tarefaService.detalharTarefa(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lista/{listaId}") // 1. Mapeamento Corrigido
    @Operation(summary = "Listar tarefas por ID da Lista", description = "Retorna todas as tarefas que pertencem a uma lista específica (coluna Kanban)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    public ResponseEntity<List<BuscaTarefa>> getTarefasPorLista( // 2. Tipo de Retorno Corrigido
        @Parameter(description = "ID da Lista") @PathVariable Long listaId) {
        try {
            List<BuscaTarefa> tarefas = tarefaService.getTarefasPorLista(listaId);
            return ResponseEntity.ok(tarefas);
        } catch (TarefaValidacaoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
    })

    public ResponseEntity<?> criarTarefa(
            @Parameter(description = "Dados para criar a tarefa") @RequestBody CadastroTarefa cadastroTarefa) {
        try {
            BuscaTarefa novaTarefa = tarefaService.criarTarefa(cadastroTarefa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
        } catch (TarefaValidacaoException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<BuscaTarefa> atualizarTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "Dados para atualizar") @RequestBody AtualizacaoTarefa atualizarDTO) {
        try {
            return tarefaService.atualizarTarefa(id, atualizarDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/mover")
    @Operation(summary = "Mover tarefa no Kanban", description = "Move uma tarefa entre colunas ou altera sua posição")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa movida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: Usuário não é membro"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PreAuthorize("hasAuthority('TAREFA_MOVER')")
    public ResponseEntity<BuscaTarefa> moverTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "Nova posição e/ou lista") @RequestBody MoverTarefaDTO moverDTO) {
        try {
            return tarefaService.moverTarefa(id, moverDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (TarefaValidacaoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> deletarTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id) {
        try {
            if (tarefaService.deletarTarefa(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar tarefas por empresa", description = "Retorna todas as tarefas de uma empresa específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<List<BuscaTarefa>> getTarefasPorEmpresa(
            @Parameter(description = "ID da empresa") @PathVariable long empresaId) {
        try {
            List<BuscaTarefa> tarefas = tarefaService.getTarefasPorEmpresa(empresaId);
            return ResponseEntity.ok(tarefas);
        } catch (TarefaValidacaoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/anexos")
    public ResponseEntity<List<BuscaAnexo>> getAnexosPorTarefa(
            @PathVariable Long id) {
        List<BuscaAnexo> anexos = tarefaService.getAnexosPorTarefa(id);
        return ResponseEntity.ok(anexos);
    }
}