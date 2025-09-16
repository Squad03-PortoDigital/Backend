package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record CadastroTarefa(
        int agenteId,
        int empresaId,
        String titulo,
        String descricao,
        PrioridadeTarefa prioridade,
        LocalDateTime dtEntrega,
        List<String> tags,
        String observacoes
) {}