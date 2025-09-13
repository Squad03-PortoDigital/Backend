package com.squad03.flap.DTO;

import com.squad03.flap.model.Cargo;
import java.util.Objects;

public class CargoDTO {

    private Long id;
    private String nome;

    // Construtores
    public CargoDTO() {}

    public CargoDTO(String nome) {
        this.nome = nome;
    }

    public CargoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor a partir da entidade
    public CargoDTO(Cargo cargo) {
        if (cargo != null) {
            this.id = cargo.getId();
            this.nome = cargo.getNome();
        }
    }

    // Converter para entidade
    public Cargo toEntity() {
        Cargo cargo = new Cargo();
        cargo.setId(this.id);
        cargo.setNome(this.nome);
        return cargo;
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
        CargoDTO cargoDTO = (CargoDTO) o;
        return Objects.equals(id, cargoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "CargoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}