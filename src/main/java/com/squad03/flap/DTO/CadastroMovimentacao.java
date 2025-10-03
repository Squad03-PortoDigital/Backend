package com.squad03.flap.DTO;

import jakarta.validation.constraints.NotNull;

public record CadastroMovimentacao(
        @NotNull(message = "A role não pode estar vazia.")
        Long role_id,
        @NotNull(message = "Tem que ter uma lista origem.")
        Long listaOrigem_id,
        @NotNull(message = "Tem que ter uma lista destino.")
        Long listaDestino_id) {
}
