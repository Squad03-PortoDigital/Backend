package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoLista;
import com.squad03.flap.DTO.BuscaLista;
import com.squad03.flap.DTO.CadastroLista;
import com.squad03.flap.service.ListaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listas")
public class ListaController {
    
    private ListaService listaService;
    
    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @PostMapping
    public ResponseEntity<BuscaLista> createLista(@RequestBody CadastroLista dados) {
        BuscaLista lista = listaService.cadastrarLista(dados);
        return new ResponseEntity<>(lista,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaLista>> findAll() {
        List<BuscaLista> listas = listaService.buscarListas();
        return ResponseEntity.ok().body(listas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaLista> findById(@PathVariable int id) {
        Optional<BuscaLista> lista = listaService.buscarListaPorId(id);
        return lista.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaLista> updateLista(@PathVariable int id, @RequestBody AtualizacaoLista dados) {
        BuscaLista lista = listaService.AtualizarLista(id, dados);
        return ResponseEntity.ok().body(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLista(@PathVariable int id) {
        listaService.excluirLista(id);
        return ResponseEntity.noContent().build();
    }
}
