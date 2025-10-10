package com.squad03.flap.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CadastroChecklist(
        @NotBlank(message = "O título do checklist não pode estar em branco.")
        @Size(min = 2, max = 255, message = "O título do checklist deve ter entre 2 e 255 caracteres.")
        String titulo,

        @NotNull(message = "O ID da tarefa é obrigatório.")
        Long tarefaId,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "A cor deve estar no formato hexadecimal (ex: #FF0000)")
        String cor
) {
}