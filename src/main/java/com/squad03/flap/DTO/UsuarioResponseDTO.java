package com.squad03.flap.DTO;

import com.squad03.flap.model.Usuario;
import java.util.Objects;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String foto;
    private CargoDTO cargo;
    private String role;  // <- adicionado

    // Construtores
    public UsuarioResponseDTO() {}

    // Construtor a partir da entidade
    public UsuarioResponseDTO(Usuario usuario) {
        if (usuario != null) {
            this.id = usuario.getId();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.foto = usuario.getFoto();
            this.role = usuario.getRole() != null ? usuario.getRole().name() : null; // <- populate role

            if (usuario.getCargo() != null) {
                this.cargo = new CargoDTO(usuario.getCargo());
            }
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public CargoDTO getCargo() { return cargo; }
    public void setCargo(CargoDTO cargo) { this.cargo = cargo; }

    public String getRole() { return role; }  // <- getter role
    public void setRole(String role) { this.role = role; }  // <- setter role

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioResponseDTO that = (UsuarioResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    // toString
    @Override
    public String toString() {
        return "UsuarioResponseDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo=" + cargo +
                ", role=" + role +  // <- incluir role
                '}';
    }
}
