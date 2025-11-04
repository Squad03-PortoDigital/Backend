package com.squad03.flap.DTO;

public record UsuarioEquipeDTO(
        Long id,
        String nome,
        String email,
        String role,  // ✅ ADICIONADO
        String foto,
        long tarefasEnvolvidas
) {
}
