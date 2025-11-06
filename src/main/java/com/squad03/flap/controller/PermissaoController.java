package com.squad03.flap.controller;

import com.squad03.flap.DTO.PermissaoDTO;
import com.squad03.flap.service.PermissaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
@CrossOrigin(origins = "*")
@Tag(name = "Permissões", description = "Gerenciamento de permissões")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;

    // Buscar todas as permissões
    @GetMapping
    public ResponseEntity<List<PermissaoDTO>> listarPermissoes() {
        try {
            List<PermissaoDTO> permissoes = permissaoService.listarPermissoes();
            return ResponseEntity.ok(permissoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<?> buscarPermissoesDoUsuario(@PathVariable Long usuarioId) {
        try {
            List<PermissaoDTO> permissoes = permissaoService.buscarPermissoesDoUsuario(usuarioId);
            return ResponseEntity.ok(permissoes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("{ \"erro\": \"" + e.getMessage() + "\" }");
        }
    }

    @GetMapping("/usuarios/{usuarioId}/nomes")
    public ResponseEntity<?> buscarNomesPermissoesDoUsuario(@PathVariable Long usuarioId) {
        try {
            List<String> permissoes = permissaoService.buscarNomesPermissoesDoUsuario(usuarioId);
            return ResponseEntity.ok(permissoes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("{ \"erro\": \"" + e.getMessage() + "\" }");
        }
    }
}
