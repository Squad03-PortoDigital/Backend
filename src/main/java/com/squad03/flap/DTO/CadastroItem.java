package com.squad03.flap.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroItem(
        @NotBlank(message = "O nome do item não pode estar em branco.")
        @Size(min = 2, max = 255, message = "O nome do item deve ter entre 2 e 255 caracteres.")
        String nome,

        @NotNull(message = "O ID da tarefa é obrigatório.")
        Long tarefaId,

        Long checklistId, // Opcional - item pode pertencer a um checklist

        Boolean status
) {
}