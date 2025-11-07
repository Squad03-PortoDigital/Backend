package com.squad03.flap.DTO;

import com.squad03.flap.model.Evento.TipoEvento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventoDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private LocalTime horario;
    private String local;
    private List<String> participantes;
    private String cor;
    private TipoEvento tipo;

    public EventoDTO(Long id, String titulo, String descricao, LocalDate data, LocalTime horario, String local, List<String> participantes, String cor, TipoEvento tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.participantes = participantes;
        this.cor = cor;
        this.tipo = tipo;
    }

    public EventoDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public LocalDate getData() {
        return this.data;
    }

    public LocalTime getHorario() {
        return this.horario;
    }

    public String getLocal() {
        return this.local;
    }

    public List<String> getParticipantes() {
        return this.participantes;
    }

    public String getCor() {
        return this.cor;
    }

    public TipoEvento getTipo() {
        return this.tipo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EventoDTO)) return false;
        final EventoDTO other = (EventoDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$titulo = this.getTitulo();
        final Object other$titulo = other.getTitulo();
        if (this$titulo == null ? other$titulo != null : !this$titulo.equals(other$titulo)) return false;
        final Object this$descricao = this.getDescricao();
        final Object other$descricao = other.getDescricao();
        if (this$descricao == null ? other$descricao != null : !this$descricao.equals(other$descricao)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        final Object this$horario = this.getHorario();
        final Object other$horario = other.getHorario();
        if (this$horario == null ? other$horario != null : !this$horario.equals(other$horario)) return false;
        final Object this$local = this.getLocal();
        final Object other$local = other.getLocal();
        if (this$local == null ? other$local != null : !this$local.equals(other$local)) return false;
        final Object this$participantes = this.getParticipantes();
        final Object other$participantes = other.getParticipantes();
        if (this$participantes == null ? other$participantes != null : !this$participantes.equals(other$participantes))
            return false;
        final Object this$cor = this.getCor();
        final Object other$cor = other.getCor();
        if (this$cor == null ? other$cor != null : !this$cor.equals(other$cor)) return false;
        final Object this$tipo = this.getTipo();
        final Object other$tipo = other.getTipo();
        if (this$tipo == null ? other$tipo != null : !this$tipo.equals(other$tipo)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EventoDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $titulo = this.getTitulo();
        result = result * PRIME + ($titulo == null ? 43 : $titulo.hashCode());
        final Object $descricao = this.getDescricao();
        result = result * PRIME + ($descricao == null ? 43 : $descricao.hashCode());
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        final Object $horario = this.getHorario();
        result = result * PRIME + ($horario == null ? 43 : $horario.hashCode());
        final Object $local = this.getLocal();
        result = result * PRIME + ($local == null ? 43 : $local.hashCode());
        final Object $participantes = this.getParticipantes();
        result = result * PRIME + ($participantes == null ? 43 : $participantes.hashCode());
        final Object $cor = this.getCor();
        result = result * PRIME + ($cor == null ? 43 : $cor.hashCode());
        final Object $tipo = this.getTipo();
        result = result * PRIME + ($tipo == null ? 43 : $tipo.hashCode());
        return result;
    }

    public String toString() {
        return "EventoDTO(id=" + this.getId() + ", titulo=" + this.getTitulo() + ", descricao=" + this.getDescricao() + ", data=" + this.getData() + ", horario=" + this.getHorario() + ", local=" + this.getLocal() + ", participantes=" + this.getParticipantes() + ", cor=" + this.getCor() + ", tipo=" + this.getTipo() + ")";
    }
}
