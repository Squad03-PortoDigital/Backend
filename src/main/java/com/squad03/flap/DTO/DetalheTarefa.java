package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import java.time.LocalDateTime;
import java.util.List;


public record DetalheTarefa(
        Long id,
        int agenteId,
        int empresaId,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        Integer posicao,
        LocalDateTime dtCriacao,
        LocalDateTime dtEntrega,
        LocalDateTime dtConclusao,

        List<String> tags,
        List<BuscaAnexo> anexos,
        List<BuscaChecklist> checklist,
        List<Object> comentarios,
        Object historico,
        String observacoes
) {}