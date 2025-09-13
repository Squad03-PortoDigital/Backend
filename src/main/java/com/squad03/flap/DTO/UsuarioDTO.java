package com.squad03.flap.DTO;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.model.Role;
import com.squad03.flap.model.Cargo;
import java.util.Objects;

public class UsuarioDTO {

    private Long id;
    private Long cargoId;
    private Long roleId;
    private String foto;
    private String nome;
    private String email;
    private String senha;

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(String nome, String email, String senha, Long roleId, Long cargoId) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roleId = roleId;
        this.cargoId = cargoId;
    }

    // Construtor a partir da entidade
    public UsuarioDTO(Usuario usuario) {
        if (usuario != null) {
            this.id = usuario.getId();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.senha = usuario.getSenha();
            this.foto = usuario.getFoto();

            if (usuario.getRole() != null) {
                this.roleId = usuario.getRole().getId();
            }

            if (usuario.getCargo() != null) {
                this.cargoId = usuario.getCargo().getId();
            }
        }
    }

    // Converter para entidade
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setFoto(this.foto);

        if (this.roleId != null) {
            Role role = new Role();
            role.setId(this.roleId);
            usuario.setRole(role);
        }

        if (this.cargoId != null) {
            Cargo cargo = new Cargo();
            cargo.setId(this.cargoId);
            usuario.setCargo(cargo);
        }

        return usuario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDTO that = (UsuarioDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", cargoId=" + cargoId +
                '}';
    }
}