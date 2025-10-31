package com.squad03.flap.DTO;

import com.squad03.flap.model.Evento.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private LocalTime horario;
    private String local;
    private List<String> participantes;
    private String cor;
    private TipoEvento tipo;
}
