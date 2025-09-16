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

    @Autowired
    private ListaService listaService;

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
    public ResponseEntity<BuscaLista> findById(@PathVariable Long id) {
        Optional<BuscaLista> lista = listaService.buscarListaPorId(id);
        return lista.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaLista> updateLista(@PathVariable Long id, @RequestBody AtualizacaoLista dados) {
        Optional<BuscaLista> lista = listaService.atualizarLista(id, dados);
        return lista.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLista(@PathVariable Long id) {
        if(listaService.excluirLista(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
