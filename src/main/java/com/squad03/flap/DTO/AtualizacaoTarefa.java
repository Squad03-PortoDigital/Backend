package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record AtualizacaoTarefa(
    String titulo,
    String descricao,
    StatusTarefa status,
    PrioridadeTarefa prioridade,
    LocalDateTime dtEntrega,
    List<String> tags,
    String observacoes
) {}
