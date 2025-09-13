package com.squad03.flap.DTO;

import com.squad03.flap.model.Role;
import java.util.Objects;

public class RoleDTO {

    private Long id;
    private String nome;

    // Construtores
    public RoleDTO() {}

    public RoleDTO(String nome) {
        this.nome = nome;
    }

    public RoleDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor a partir da entidade
    public RoleDTO(Role role) {
        if (role != null) {
            this.id = role.getId();
            this.nome = role.getNome();
        }
    }

    // Converter para entidade
    public Role toEntity() {
        Role role = new Role();
        role.setId(this.id);
        role.setNome(this.nome);
        return role;
    }

    // Getters e Setters
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

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(id, roleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}