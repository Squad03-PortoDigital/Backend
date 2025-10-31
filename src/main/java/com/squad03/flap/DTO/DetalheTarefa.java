package com.squad03.flap.DTO;

import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import java.time.LocalDateTime;
import java.util.List;

public record DetalheTarefa(
        Long id,
        Long empresaId,
        String empresa,          // ✅ NOME DA EMPRESA
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
        List<BuscaAnexo> anexos,
        List<BuscaChecklist> checklist,
        List<ComentarioResponseDTO> comentarios,
        List<MembroSimplificadoDTO> membros, // ✅ DETALHES DOS MEMBROS
        Object historico,
        String observacoes
) {}
