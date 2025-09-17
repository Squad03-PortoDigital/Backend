package com.squad03.flap.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CadastroEmpresa(@NotBlank(message = "O nome não pode estar em branco.")
                              @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
                              String nome,

                              @NotBlank(message = "O CNPJ не pode estar em branco.")
                              @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter apenas 14 números.")
                              String cnpj,

                              @NotBlank(message = "O e-mail não pode estar em branco.")
                              @Email(message = "O formato do e-mail é inválido.")
                              String email,

                              @NotBlank(message = "O contato не pode estar em branco.")
                              @Size(min = 10, max = 15, message = "O contato deve ter entre 10 e 15 caracteres (com DDD).")
                              String contato,

                              @NotBlank(message = "A área de atuação не pode estar em branco.")
                              String atuacao,

                              String observacao,

                              String foto)
                              {
}
