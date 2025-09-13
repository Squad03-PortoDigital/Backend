package com.squad03.flap.controller;

import com.squad03.flap.DTO.BuscaAgente;
import com.squad03.flap.DTO.CadastroAnexo;
import com.squad03.flap.DTO.BuscaAnexo;
import com.squad03.flap.service.AnexoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/anexos")
public class AnexoController {

    @Autowired
    private AnexoService anexoService;

    @PostMapping
    public ResponseEntity<BuscaAnexo> salvarAnexo(@RequestBody CadastroAnexo dados) {
        BuscaAnexo anexoSalvo = anexoService.salvarAnexo(dados);
        return new ResponseEntity<>(anexoSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaAnexo>> listarAnexos() {
        List<BuscaAnexo> anexos = anexoService.listarTodos();
        return ResponseEntity.ok(anexos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaAnexo> buscarAnexoPorId(@PathVariable Long id) {
        Optional<BuscaAnexo> anexo = anexoService.buscarPorId(id);
        return anexo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAnexo(@PathVariable Long id) {
        anexoService.deletarAnexo(id);
        return ResponseEntity.noContent().build();
    }
}
