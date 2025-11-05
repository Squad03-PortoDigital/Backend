package com.squad03.flap.DTO;

import com.squad03.flap.model.Checklist;
import java.util.List;
import java.util.stream.Collectors;

public class ChecklistResponseDTO {
    private Long id;
    private String titulo;
    private String cor;
    private Long tarefaId;
    private List<ItemDTO> itens;

    public ChecklistResponseDTO() {}

    // ✅ CONSTRUTOR que o IntelliJ está reclamando
    public ChecklistResponseDTO(Checklist checklist) {
        this.id = checklist.getId();
        this.titulo = checklist.getTitulo();
        this.cor = checklist.getCor();
        this.tarefaId = checklist.getTarefa().getId();
        this.itens = checklist.getItens() != null
                ? checklist.getItens().stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList())
                : List.of();
    }



    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCor() { return cor; }
    public Long getTarefaId() { return tarefaId; }
    public List<ItemDTO> getItens() { return itens; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setCor(String cor) { this.cor = cor; }
    public void setTarefaId(Long tarefaId) { this.tarefaId = tarefaId; }
    public void setItens(List<ItemDTO> itens) { this.itens = itens; }

    // ✅ ItemDTO interno
    public static class ItemDTO {
        private Long id;
        private String nome;
        private Boolean status;

        public ItemDTO() {}

        public ItemDTO(com.squad03.flap.model.Item item) {
            this.id = item.getId();
            this.nome = item.getNome();
            this.status = item.getStatus();
        }

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public Boolean getStatus() { return status; }

        public void setId(Long id) { this.id = id; }
        public void setNome(String nome) { this.nome = nome; }
        public void setStatus(Boolean status) { this.status = status; }
    }
}
