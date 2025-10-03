package com.squad03.flap.DTO;


import jakarta.validation.constraints.NotNull;

public class MembroCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID da tarefa é obrigatório")
    private Long tarefaId;

    public MembroCreateDTO() {}

    public MembroCreateDTO(Long usuarioId, Long tarefaId) {
        this.usuarioId = usuarioId;
        this.tarefaId = tarefaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }
}