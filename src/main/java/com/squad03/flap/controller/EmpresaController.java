package com.squad03.flap.controller;

import com.squad03.flap.DTO.AtualizacaoEmpresa;
import com.squad03.flap.DTO.CadastroEmpresa;
import com.squad03.flap.DTO.BuscaEmpresa;
import com.squad03.flap.service.EmpresaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Gerenciamento das empresas")
public class EmpresaController {

    private EmpresaService empresaService;

    @Autowired
    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<BuscaEmpresa> createEmpresa(@RequestBody @Valid CadastroEmpresa dados) {
        BuscaEmpresa empresaSalva = empresaService.cadastrarEmpresa(dados);
        return new ResponseEntity<>(empresaSalva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaEmpresa>> findAll() {
        List<BuscaEmpresa> empresas = empresaService.buscarEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaEmpresa> findById(@PathVariable Long id) {
        Optional<BuscaEmpresa> empresa = empresaService.buscarEmpresaPorId(id);
        return empresa.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaEmpresa> updateEmpresa(@PathVariable Long id, @RequestBody AtualizacaoEmpresa dados) {
        BuscaEmpresa empresa = empresaService.AtualizarEmpresa(id, dados);
        return ResponseEntity.ok(empresa);    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.excluirEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}
