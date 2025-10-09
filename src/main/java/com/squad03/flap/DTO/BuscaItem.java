package com.squad03.flap.DTO;

import com.squad03.flap.model.Item;

public record BuscaItem(
        Long id,
        String nome,
        Boolean status,
        Long tarefaId,
        Long checklistId
) {
    public BuscaItem(Item item) {
        this(
            item.getId(),
            item.getNome(),
            item.getStatus(),
            item.getTarefa().getId(),
            item.getChecklist() != null ? item.getChecklist().getId() : null
        );
    }
}