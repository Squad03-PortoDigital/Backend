package com.squad03.flap.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComentarioRequestDTO {

    @NotBlank(message = "O texto do comentário é obrigatório")
    private String texto;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID da tarefa é obrigatório")
    private Long tarefaId;

    public ComentarioRequestDTO() {}

    public ComentarioRequestDTO(String texto, Long usuarioId, Long tarefaId) {
        this.texto = texto;
        this.usuarioId = usuarioId;
        this.tarefaId = tarefaId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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