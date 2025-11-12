package com.squad03.flap.DTO;

public class RemetenteDTO {
    private Long id;
    private String nome;
    private String foto;

    // ===== Construtores =====

    public RemetenteDTO() {
    }

    public RemetenteDTO(Long id, String nome, String foto) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
    }

    // ===== Getters e Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
