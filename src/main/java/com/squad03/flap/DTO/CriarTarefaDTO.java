package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CriarTarefaDTO(

    @NotNull(message = "O ID do agente não pode ser nulo.")
    int agenteId,

    String titulo,
    String descricao,
    PrioridadeTarefa prioridade,
    LocalDateTime dtEntrega,
    List<String> tags,
    String observacoes,

    @NotNull(message = "O ID da empresa não pode ser nulo.")
    Long empresaId,

    @NotNull(message = "O ID da lista não pode ser nulo.")
    Long listaId
) {}
