package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "Cargo_id")
    private Cargo cargo;

    private String foto;
    private String nome;
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioQuadro> usuarioQuadros;
    public Usuario() {
    }

    public Usuario (Cargo cargo, String foto, String nome, String email) {
        this.cargo = cargo;
        this.foto = foto;
        this.nome = nome;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public Cargo getCargo() {
        return cargo;
    }
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
