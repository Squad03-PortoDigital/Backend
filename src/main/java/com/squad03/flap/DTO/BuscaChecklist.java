package com.squad03.flap.DTO;

import com.squad03.flap.model.Checklist;

public record BuscaChecklist(
        Long id,
        String titulo,
        String cor,
        Long tarefaId,
        Integer totalItens,
        Integer itensCompletos
) {
    public BuscaChecklist(Checklist checklist) {
        this(
            checklist.getId(),
            checklist.getTitulo(),
            checklist.getCor(),
            checklist.getTarefa().getId(),
            checklist.getItens() != null ? checklist.getItens().size() : 0,
            checklist.getItens() != null ? (int) checklist.getItens().stream()
                .filter(item -> item.getStatus() != null && item.getStatus()).count() : 0
        );
    }
}