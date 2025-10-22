package com.squad03.flap.DTO;

import com.squad03.flap.model.Role;
import java.util.Set;
import java.util.stream.Collectors;

public record BuscaRole(
        Long id,
        String nome,

        Set<PermissaoDTO> permissoes
) {
    public BuscaRole(Role role) {
        this(
                role.getId(),
                role.getNome(),

                role.getPermissoes().stream()
                        .map(PermissaoDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}