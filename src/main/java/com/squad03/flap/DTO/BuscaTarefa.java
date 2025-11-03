package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record BuscaTarefa(
        Long id,
        Long empresaId,
        String empresa,
        Long listaId,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        Double posicao,
        LocalDateTime dtCriacao,
        LocalDateTime dtEntrega,
        LocalDateTime dtConclusao,
        List<String> tags,
        String observacoes,
        List<Long> membroIds,        // IDs dos registros na tabela Membro
        List<Long> usuarioIds,       // IDs dos Usuários (para filtro no frontend)
        List<MembroSimplificadoDTO> membros  // Detalhes completos dos membros
) {}
