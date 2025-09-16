package com.squad03.flap.DTO;



import java.util.Objects;

public class LoginDTO {

    private String email;
    private String senha;

    // Construtores
    public LoginDTO() {}

    public LoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
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
        LoginDTO loginDTO = (LoginDTO) o;
        return Objects.equals(email, loginDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    // toString
    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}