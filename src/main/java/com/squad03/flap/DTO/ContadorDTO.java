package com.squad03.flap.DTO;

import java.util.Objects;

public class ContadorDTO {

    private Long quantidade;
    private String tipo;

    // Construtores
    public ContadorDTO() {}

    public ContadorDTO(Long quantidade) {
        this.quantidade = quantidade;
    }

    public ContadorDTO(Long quantidade, String tipo) {
        this.quantidade = quantidade;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContadorDTO that = (ContadorDTO) o;
        return Objects.equals(quantidade, that.quantidade) && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantidade, tipo);
    }

    // toString
    @Override
    public String toString() {
        return "ContadorDTO{" +
                "quantidade=" + quantidade +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}