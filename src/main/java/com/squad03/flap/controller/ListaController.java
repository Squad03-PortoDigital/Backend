package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoLista;
import com.squad03.flap.DTO.BuscaLista;
import com.squad03.flap.DTO.CadastroLista;
import com.squad03.flap.model.Lista;
import com.squad03.flap.service.ListaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Transactional
    public void createLista(@RequestBody CadastroLista dados) {
        listaService.cadastrarLista(dados);
    }

    @GetMapping
    public List<BuscaLista> findAll() {
        return listaService.buscarListas();
    }

    @GetMapping("/{id}")
    public Lista findById(@RequestParam int id) {
        return listaService.buscarListaPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void updateLista(@PathVariable int id, @RequestBody AtualizacaoLista dados) {
        listaService.AtualizarLista(id, dados);
    }

    @DeleteMapping
    @Transactional
    public void deleteLista(@PathVariable int id) {
        listaService.excluirLista(id);
    }
}
