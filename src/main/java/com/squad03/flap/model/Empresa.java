package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroEmpresa;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
    private String cnpj;
    private String email;
    private String contato;
    private String atuacao;
    private String observacao;
    private String foto;

    @Column(nullable = false, length = 250)
    private String agenteLink;

    // ✅ NOVO CAMPO
    @Column(nullable = false)
    private Boolean arquivada = false;

    // ✅ NOVO CAMPO - Data de criação
    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tarefa;

    // ✅ Hook executado antes de persistir
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (arquivada == null) {
            arquivada = false;
        }
    }

    public Empresa() {}

    public Empresa(String nome, String foto) {
        this.nome = nome;
        this.foto = foto;
        this.arquivada = false;
    }

    public Empresa(CadastroEmpresa cadastroEmpresa) {
        this.nome = cadastroEmpresa.nome();
        this.cnpj = cadastroEmpresa.cnpj();
        this.email = cadastroEmpresa.email();
        this.contato = cadastroEmpresa.contato();
        this.atuacao = cadastroEmpresa.atuacao();
        this.observacao = cadastroEmpresa.observacao();
        this.foto = cadastroEmpresa.foto();
        this.agenteLink = cadastroEmpresa.agenteLink();
        this.arquivada = false;
    }

    // ===== GETTERS E SETTERS =====

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

    public String getAgenteLink() {
        return agenteLink;
    }

    public void setAgenteLink(String agenteLink) {
        this.agenteLink = agenteLink;
    }

    public List<Tarefa> getTarefa() {
        return tarefa;
    }

    // ✅ NOVOS GETTERS E SETTERS

    public Boolean getArquivada() {
        return arquivada;
    }

    public void setArquivada(Boolean arquivada) {
        this.arquivada = arquivada;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
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
