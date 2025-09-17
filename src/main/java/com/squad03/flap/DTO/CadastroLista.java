package com.squad03.flap.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CadastroLista(
        @NotBlank(message = "O nome não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotNull
        @Positive
        int posicao,

        String cor) {
}
