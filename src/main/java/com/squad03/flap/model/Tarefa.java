package com.squad03.flap.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // @OneToMany(mappedBy = "empresa")
   // @JoinColumn(name = "empresa_id")
   // private Empresa empresa;

   // @OneToMany(mappedBy = "lista")
   // @JoinColumn(name = "lista_id")
   // private Lista lista;

   @OneToMany(mappedBy = "agente")
   @JoinColumn(name = "agente_id")
   private Agente agente;


    private String nome;
    private String titulo;
    private String descricao;
    private String posicao;
    private Date dt_Criacao;
    private Date dt_entrega;

}