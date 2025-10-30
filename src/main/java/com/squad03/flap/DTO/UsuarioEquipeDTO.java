package com.squad03.flap.DTO;

public record UsuarioEquipeDTO(Long id, String nome,
       String email,
       String cargoNome,
       String foto,
       long tarefasEnvolvidas) {
}
