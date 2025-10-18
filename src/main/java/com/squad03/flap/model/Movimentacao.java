package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroMovimentacao;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "listaOrigem")
    private Lista listaOrigem;

    @ManyToOne
    @JoinColumn(name = "listaDestino")
    private Lista listaDestino;

    public Movimentacao() {}

    public Movimentacao(Role role, Lista listaOrigem, Lista listaDestino) {
        this.role = role;
       this.listaOrigem = listaOrigem;
        this.listaDestino = listaDestino;
    }

    public long getId() {
        return id;
    }

    public Lista getListaOrigem() {
        return listaOrigem;
    }

    public void setListaOrigem(Lista listaOrigem) {
        this.listaOrigem = listaOrigem;
    }

    public Lista getListaDestino() {
        return listaDestino;
    }

    public void setListaDestino(Lista listaDestino) {
        this.listaDestino = listaDestino;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Movimentacao that)) return false;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
