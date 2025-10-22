package com.squad03.flap.DTO;

import com.squad03.flap.model.Permissao;

public record PermissaoDTO(
        Long id,
        String nome
) {
    public PermissaoDTO(Permissao permissao) {
        this(
                permissao.getId(),
                permissao.getNome()
        );
    }
}