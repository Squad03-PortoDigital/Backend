package com.squad03.flap.DTO;

public class TarefaEventoDTO {
    private String tipo; // "CRIADA", "MOVIDA", "ATUALIZADA", "DELETADA"
    private Long tarefaId;
    private Long listaId;
    private Long listaIdOrigem;
    private Integer novaPosicao;
    private BuscaTarefa tarefa;
    private String usuarioNome;

    public TarefaEventoDTO() {
    }

    public TarefaEventoDTO(String tipo, Long tarefaId, Long listaId, Long listaIdOrigem, Integer novaPosicao, BuscaTarefa tarefa, String usuarioNome) {
        this.tipo = tipo;
        this.tarefaId = tarefaId;
        this.listaId = listaId;
        this.listaIdOrigem = listaIdOrigem;
        this.novaPosicao = novaPosicao;
        this.tarefa = tarefa;
        this.usuarioNome = usuarioNome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getListaIdOrigem() { return listaIdOrigem; }

    public void setListaIdOrigem(Long listaIdOrigem) { this.listaIdOrigem = listaIdOrigem; }

    public Long getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }

    public Long getListaId() {
        return listaId;
    }

    public void setListaId(Long listaId) {
        this.listaId = listaId;
    }

    public Integer getNovaPosicao() {
        return novaPosicao;
    }

    public void setNovaPosicao(Integer novaPosicao) {
        this.novaPosicao = novaPosicao;
    }

    public BuscaTarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(BuscaTarefa tarefa) {
        this.tarefa = tarefa;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }
}
