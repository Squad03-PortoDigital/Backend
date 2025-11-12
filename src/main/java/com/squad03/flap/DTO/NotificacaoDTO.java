package com.squad03.flap.DTO;

import com.squad03.flap.model.TipoNotificacao;
import java.time.LocalDateTime;

public class NotificacaoDTO {
    private Long id;
    private TipoNotificacao tipo;
    private String titulo;
    private String mensagem;
    private Long tarefaId;
    private String tarefaTitulo;
    private RemetenteDTO remetente;
    private Boolean lida;
    private LocalDateTime dataHora;

    // ===== Construtores =====

    public NotificacaoDTO() {
    }

    public NotificacaoDTO(Long id, TipoNotificacao tipo, String titulo, String mensagem,
                          Long tarefaId, String tarefaTitulo, RemetenteDTO remetente,
                          Boolean lida, LocalDateTime dataHora) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tarefaId = tarefaId;
        this.tarefaTitulo = tarefaTitulo;
        this.remetente = remetente;
        this.lida = lida;
        this.dataHora = dataHora;
    }

    // ===== Getters e Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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

    public RemetenteDTO getRemetente() {
        return remetente;
    }

    public void setRemetente(RemetenteDTO remetente) {
        this.remetente = remetente;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
