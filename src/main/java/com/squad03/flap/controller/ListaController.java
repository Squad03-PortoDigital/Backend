package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoLista;
import com.squad03.flap.DTO.BuscaLista;
import com.squad03.flap.DTO.CadastroLista;
import com.squad03.flap.service.ListaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listas")
@Tag(name = "Listas", description = "Gerenciamento de listas")
public class ListaController {
    
    private ListaService listaService;
    
    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('LISTA_GERENCIAR')")
    public ResponseEntity<BuscaLista> createLista(@RequestBody @Valid CadastroLista dados) {
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
    @PreAuthorize("hasAuthority('LISTA_GERENCIAR')")
    public ResponseEntity<BuscaLista> updateLista(@PathVariable Long id, @RequestBody AtualizacaoLista dados) {
        // O service retorna Optional<BuscaLista>
        return listaService.atualizarLista(id, dados)
                // Se o Optional tiver valor (Lista atualizada): retorna 200 OK
                .map(ResponseEntity::ok)
                // Se o Optional estiver vazio (Lista não encontrada): retorna 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LISTA_GERENCIAR')")
    public ResponseEntity<Void> deleteLista(@PathVariable Long id) {
        listaService.excluirLista(id);
        return ResponseEntity.noContent().build();
    }

}
