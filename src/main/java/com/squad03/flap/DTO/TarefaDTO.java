package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record TarefaDTO(
    Long id,
    Long agenteId,
    String titulo,
    String descricao,
    StatusTarefa status,
    PrioridadeTarefa prioridade,
    Integer posicao,
    LocalDateTime dtCriacao,
    LocalDateTime dtEntrega,
    LocalDateTime dtConclusao,
    List<String> tags,
    String observacoes,
    Long empresa_id,
    Long lista_id
) {}
