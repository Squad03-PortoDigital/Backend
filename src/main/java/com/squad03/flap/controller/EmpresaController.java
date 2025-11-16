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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    @PreAuthorize("hasAuthority('EMPRESA_CRIAR')")
    public ResponseEntity<BuscaEmpresa> createEmpresa(@RequestBody @Valid CadastroEmpresa dados) {
        BuscaEmpresa empresaSalva = empresaService.cadastrarEmpresa(dados);
        return new ResponseEntity<>(empresaSalva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaEmpresa>> findAll(
            @RequestParam(required = false) Boolean arquivada
    ) {
        List<BuscaEmpresa> empresas;

        if (arquivada != null) {
            empresas = empresaService.buscarEmpresasPorStatus(arquivada);
        } else {
            empresas = empresaService.buscarEmpresas();
        }

        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaEmpresa> findById(@PathVariable Long id) {
        Optional<BuscaEmpresa> empresa = empresaService.buscarEmpresaPorId(id);
        return empresa.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPRESA_EDITAR')")
    public ResponseEntity<BuscaEmpresa> updateEmpresa(@PathVariable Long id, @RequestBody AtualizacaoEmpresa dados) {
        BuscaEmpresa empresa = empresaService.AtualizarEmpresa(id, dados);
        return ResponseEntity.ok(empresa);
    }

    // ✅ NOVO ENDPOINT - Arquivar/Desarquivar empresa
    @PatchMapping("/{id}/arquivar")
    @PreAuthorize("hasAuthority('EMPRESA_EDITAR')")
    public ResponseEntity<?> arquivarEmpresa(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request
    ) {
        try {
            Boolean arquivada = request.get("arquivada");
            if (arquivada == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Campo 'arquivada' é obrigatório"));
            }

            BuscaEmpresa empresa = empresaService.arquivarEmpresa(id, arquivada);

            return ResponseEntity.ok(Map.of(
                    "message", arquivada ? "Empresa arquivada com sucesso" : "Empresa restaurada com sucesso",
                    "empresa", empresa
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao arquivar empresa: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPRESA_EDITAR')")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.excluirEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}
