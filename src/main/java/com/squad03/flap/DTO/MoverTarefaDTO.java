package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.StatusTarefa;

public record MoverTarefaDTO(
    Integer novaPosicao,
    StatusTarefa novoStatus
) {}
