package com.squad03.flap.DTO;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CadastroRole(

        @NotBlank(message = "O nome da Role é obrigatório.")
        @Size(max = 50, message = "O nome não pode exceder 50 caracteres.")
        String nome,

        @NotEmpty(message = "A Role deve ter pelo menos uma Permissão associada.")
        Set<Long> permissoesIds
) {}