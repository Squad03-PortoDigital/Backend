package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoEmpresa;
import com.squad03.flap.DTO.CadastroEmpresa;
import com.squad03.flap.DTO.BuscaEmpresa;
import com.squad03.flap.model.Empresa;
import com.squad03.flap.service.EmpresaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Gerenciamento de empresas")
public class EmpresaController {

    private EmpresaService empresaService;

    @Autowired
    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @Transactional
    public void createEmpresa(@RequestBody CadastroEmpresa dados) {
        empresaService.cadastrarEmpresa(dados);
    }

    @GetMapping
    public List<BuscaEmpresa> findAll() {
        return empresaService.buscarEmpresas();
    }

    @GetMapping("/{id}")
    public Empresa findById(@RequestParam int id) {
        return empresaService.buscarEmpresaPorId(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void updateEmpresa(@PathVariable int id, @RequestBody AtualizacaoEmpresa dados) {
        empresaService.AtualizarEmpresa(id, dados);
    }

    @DeleteMapping
    @Transactional
    public void deleteEmpresa(@PathVariable int id) {
        empresaService.excluirEmpresa(id);
    }
}
