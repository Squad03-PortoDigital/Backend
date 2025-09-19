package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroEmpresa;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cnpj;

    private String email;

    private String contato;

    private String atuacao;

    private String observacao;

    private String foto;

    @OneToMany(mappedBy = "empresas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tarefa;

    public Empresa() {}
    public Empresa(String nome, String foto) {
        this.nome = nome;
        this.foto = foto;
    }

    public Empresa(CadastroEmpresa cadastroEmpresa) {
        this.nome = cadastroEmpresa.nome();
        this.cnpj = cadastroEmpresa.cnpj();
        this.email = cadastroEmpresa.email();
        this.contato = cadastroEmpresa.contato();
        this.atuacao = cadastroEmpresa.atuacao();
        this.observacao = cadastroEmpresa.observacao();
        this.foto = cadastroEmpresa.foto();
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getAtuacao() {
        return atuacao;
    }

    public void setAtuacao(String atuacao) {
        this.atuacao = atuacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Tarefa> getTarefa() {
        return tarefa;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Empresa empresa)) return false;

        return id.equals(empresa.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
