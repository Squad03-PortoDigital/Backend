package com.squad03.flap.DTO;

import jakarta.validation.constraints.Size;

public record AtualizacaoItem(
        @Size(min = 2, max = 255, message = "O nome do item deve ter entre 2 e 255 caracteres.")
        String nome,

        Boolean status
) {
}