package com.squad03.flap.DTO;

import com.squad03.flap.model.Anexo;

public record BuscaAnexo(
        Long id,
        String link,
        String descricao,
        Long tarefaId) {

    public BuscaAnexo(Anexo anexo) {
        this(
                anexo.getId(),
                anexo.getLink(),
                anexo.getDescricao(),
                anexo.getTarefa().getId()
        );
    }
}
