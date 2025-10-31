package com.squad03.flap.controller;

import com.squad03.flap.DTO.EventoDTO;
import com.squad03.flap.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "API para gerenciamento de eventos do calendário")
@CrossOrigin(origins = "*")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping
    @Operation(summary = "Listar todos os eventos")
    public ResponseEntity<List<EventoDTO>> listarTodos() {
        List<EventoDTO> eventos = eventoService.listarTodos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }

    @GetMapping("/data/{data}")
    @Operation(summary = "Buscar eventos por data")
    public ResponseEntity<List<EventoDTO>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<EventoDTO> eventos = eventoService.buscarPorData(data);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/mes/{mes}/ano/{ano}")
    @Operation(summary = "Buscar eventos por mês e ano")
    public ResponseEntity<List<EventoDTO>> buscarPorMesEAno(
            @PathVariable int mes,
            @PathVariable int ano) {
        List<EventoDTO> eventos = eventoService.buscarPorMesEAno(mes, ano);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/futuros")
    @Operation(summary = "Buscar eventos futuros")
    public ResponseEntity<List<EventoDTO>> buscarEventosFuturos() {
        List<EventoDTO> eventos = eventoService.buscarEventosFuturos();
        return ResponseEntity.ok(eventos);
    }

    @PostMapping
    @Operation(summary = "Criar novo evento")
    public ResponseEntity<EventoDTO> criar(@RequestBody EventoDTO eventoDTO) {
        EventoDTO novoEvento = eventoService.criar(eventoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento")
    public ResponseEntity<EventoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoAtualizado = eventoService.atualizar(id, eventoDTO);
        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
