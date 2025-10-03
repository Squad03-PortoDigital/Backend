package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoMovimentacao;
import com.squad03.flap.DTO.BuscaMovimentacao;
import com.squad03.flap.DTO.CadastroMovimentacao;
import com.squad03.flap.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {
    private MovimentacaoService movimentacaoService;

    @Autowired
    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<BuscaMovimentacao> createMovimentacao(@RequestBody @Valid CadastroMovimentacao dados) {
        BuscaMovimentacao movimentacao = movimentacaoService.cadastrarMovimentacao(dados);
        return new ResponseEntity<>(movimentacao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaMovimentacao>> findAll() {
        List<BuscaMovimentacao> movimentacaos = movimentacaoService.buscarMovimentacoes();
        return ResponseEntity.ok().body(movimentacaos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaMovimentacao> findById(@PathVariable Long id) {
        Optional<BuscaMovimentacao> movimentacao = movimentacaoService.buscarMovimentacaoPorId(id);
        return movimentacao.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaMovimentacao> updateMovimentacao(@PathVariable Long id, @RequestBody AtualizacaoMovimentacao dados) {
        BuscaMovimentacao movimentacao = movimentacaoService.atualizarMovimentacao(id, dados);
        return ResponseEntity.ok().body(movimentacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimentacao(@PathVariable Long id) {
        movimentacaoService.deletarMovimentacao(id);
        return ResponseEntity.noContent().build();
    }
}
