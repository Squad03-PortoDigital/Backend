package com.squad03.flap.DTO;

import com.squad03.flap.model.Membro;

public class MembroDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private Long tarefaId;
    private String tarefaTitulo;

    public MembroDTO() {}

    public static MembroDTO fromEntity(Membro membro) {
        MembroDTO dto = new MembroDTO();
        dto.setId(membro.getId());

        if (membro.getUsuario() != null) {
            dto.setUsuarioId(membro.getUsuario().getId());
            dto.setUsuarioNome(membro.getUsuario().getNome());
            dto.setUsuarioEmail(membro.getUsuario().getEmail());
        }

        if (membro.getTarefa() != null) {
            dto.setTarefaId(membro.getTarefa().getId());
            dto.setTarefaTitulo(membro.getTarefa().getTitulo());
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public Long getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }

    public String getTarefaTitulo() {
        return tarefaTitulo;
    }

    public void setTarefaTitulo(String tarefaTitulo) {
        this.tarefaTitulo = tarefaTitulo;
    }
}