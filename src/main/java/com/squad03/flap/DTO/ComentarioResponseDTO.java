package com.squad03.flap.DTO;


import java.time.LocalDateTime;

public class ComentarioResponseDTO {

    private Long id;
    private String texto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long usuarioId;
    private String usuarioNome;
    private Long tarefaId;
    private String tarefaTitulo;

    public ComentarioResponseDTO() {}

    public ComentarioResponseDTO(Long id, String texto, LocalDateTime dataCriacao,
                                 LocalDateTime dataAtualizacao, Long usuarioId,
                                 String usuarioNome, Long tarefaId, String tarefaTitulo) {
        this.id = id;
        this.texto = texto;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.tarefaId = tarefaId;
        this.tarefaTitulo = tarefaTitulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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