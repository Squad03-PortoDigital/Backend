package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroLista;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Lista")
public class Lista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int posicao;
    private String cor;

    @OneToMany(mappedBy = "lista")
    private List<Tarefa> tarefa;

    @OneToMany(mappedBy = "listaOrigem")
    private List<Movimentacao> movimentacaoOrigem;

    @OneToMany(mappedBy = "listaDestino")
    private List<Movimentacao> movimentacaoDestino;

    public Lista() {
    }

    public Lista(String nome, int posicao, String cor) {
        this.nome = nome;
        this.posicao = posicao;
        this.cor = cor;
    }

    public Lista(CadastroLista cadastroLista) {
        this.nome = cadastroLista.nome();
        this.posicao = cadastroLista.posicao();
        this.cor = cadastroLista.cor();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public List<Tarefa> getTarefa() {
        return tarefa;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Lista lista)) return false;

        return id.equals(lista.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
