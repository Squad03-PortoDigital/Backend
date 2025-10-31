package com.squad03.flap.DTO;

public record MembroSimplificadoDTO(
        Long membroId,      // ID do registro na tabela Membro
        Long usuarioId,     // ID do Usuário
        String nome,        // Nome do usuário
        String username,    // Username do usuário (email)
        String foto         // Foto do usuário
) {}
