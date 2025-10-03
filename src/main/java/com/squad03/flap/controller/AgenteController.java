package com.squad03.flap.controller;

import com.squad03.flap.DTO.CadastroAgente;
import com.squad03.flap.DTO.BuscaAgente;
import com.squad03.flap.service.AgenteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agente")
@Tag(name = "Agentes", description = "Gerenciamento dos agentes")
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @PostMapping
    public ResponseEntity<BuscaAgente> salvarAgente(@RequestBody CadastroAgente dados) {
        BuscaAgente agenteSalvo = agenteService.salvarAgente(dados);
        return new ResponseEntity<>(agenteSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaAgente>> listarAgentes() {
        List<BuscaAgente> agentes = agenteService.listarTodos();
        return ResponseEntity.ok(agentes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaAgente> buscarAgentePorId(@PathVariable int id) {
        Optional<BuscaAgente> agente = agenteService.buscarPorId(id);
        return agente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgente(@PathVariable int id) {
        agenteService.deletarAgente(id);
        return ResponseEntity.noContent().build();
    }
}
