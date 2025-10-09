package com.squad03.flap.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AtualizacaoChecklist(
        @Size(min = 2, max = 255, message = "O título do checklist deve ter entre 2 e 255 caracteres.")
        String titulo,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "A cor deve estar no formato hexadecimal (ex: #FF0000)")
        String cor
) {
}