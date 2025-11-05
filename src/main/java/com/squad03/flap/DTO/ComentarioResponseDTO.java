package com.squad03.flap.DTO;

import com.squad03.flap.model.Comentario;
import java.time.LocalDateTime;

public class ComentarioResponseDTO {

    private Long id;
    private String texto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long tarefaId;
    private String tarefaTitulo;
    private UsuarioComentarioDTO usuario;  // ✅ NOVO - Objeto aninhado

    public ComentarioResponseDTO() {}

    // ✅ Construtor a partir da entidade
    public ComentarioResponseDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.texto = comentario.getTexto();
        this.dataCriacao = comentario.getDataCriacao();
        this.dataAtualizacao = comentario.getDataAtualizacao();
        this.tarefaId = comentario.getTarefa().getId();
        this.tarefaTitulo = comentario.getTarefa().getTitulo();

        // ✅ Monta o objeto usuario
        if (comentario.getUsuario() != null) {
            this.usuario = new UsuarioComentarioDTO(
                    comentario.getUsuario().getId(),
                    comentario.getUsuario().getNome(),
                    comentario.getUsuario().getEmail(),
                    comentario.getUsuario().getFoto()
            );
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Long getTarefaId() { return tarefaId; }
    public void setTarefaId(Long tarefaId) { this.tarefaId = tarefaId; }

    public String getTarefaTitulo() { return tarefaTitulo; }
    public void setTarefaTitulo(String tarefaTitulo) { this.tarefaTitulo = tarefaTitulo; }

    public UsuarioComentarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioComentarioDTO usuario) { this.usuario = usuario; }

    // ✅ CLASSE INTERNA para o usuario do comentário
    public static class UsuarioComentarioDTO {
        private Long id;
        private String nome;
        private String email;
        private String foto;

        public UsuarioComentarioDTO(Long id, String nome, String email, String foto) {
            this.id = id;
            this.nome = nome;
            this.email = email;
            this.foto = foto;
        }

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getFoto() { return foto; }
    }
}
