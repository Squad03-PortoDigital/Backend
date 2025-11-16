package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record AtualizacaoTarefa(
        Long empresaId,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDateTime dtEntrega,
        Boolean concluida,  // ✅ NOVO
        List<String> tags,
        String observacoes,
        List<Long> membroIds
) {}
