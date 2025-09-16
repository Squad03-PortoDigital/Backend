package com.squad03.flap.DTO;

import java.util.Objects;

public class FotoDTO {

    private String foto;

    // Construtores
    public FotoDTO() {}

    public FotoDTO(String foto) {
        this.foto = foto;
    }

    // Getter e Setter
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FotoDTO fotoDTO = (FotoDTO) o;
        return Objects.equals(foto, fotoDTO.foto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foto);
    }

    // toString
    @Override
    public String toString() {
        return "FotoDTO{" +
                "foto='" + (foto != null ? foto.substring(0, Math.min(foto.length(), 50)) + "..." : null) + '\'' +
                '}';
    }
}
