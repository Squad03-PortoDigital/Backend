package com.squad03.flap.controller;

import com.squad03.flap.DTO.*;
import com.squad03.flap.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Roles", description = "Gerenciamento dos roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Criar nova role
    @PostMapping
    public ResponseEntity<?> salvarRole(@RequestBody CadastroRole dados) {
        try {
            // ✅ Validações
            if (dados.nome() == null || dados.nome().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Nome da role é obrigatório"));
            }

            if (dados.permissoesIds() == null || dados.permissoesIds().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Role deve ter pelo menos uma permissão"));
            }

            BuscaRole roleSalva = roleService.cadastrarRole(dados);
            return new ResponseEntity<>(roleSalva, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar role: " + e.getMessage()));
        }
    }

    // Listar todas as roles
    @GetMapping
    public ResponseEntity<List<BuscaRole>> listarRoles() {
        try {
            List<BuscaRole> roles = roleService.buscarRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar role por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarRolePorId(@PathVariable long id) {
        try {
            Optional<BuscaRole> role = roleService.buscaRolePorId(id);
            return role.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao buscar role: " + e.getMessage()));
        }
    }

    // Atualizar role
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarRole(@PathVariable Long id, @RequestBody AtualizacaoRole dados) {
        try {
            // ✅ Validações
            if (dados.nome() != null && dados.nome().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Nome não pode estar vazio"));
            }

            if (dados.permissoesIds() != null && dados.permissoesIds().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Role deve ter pelo menos uma permissão"));
            }

            Optional<BuscaRole> roleAtualizada = roleService.atualizarRole(id, dados);
            return roleAtualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar role: " + e.getMessage()));
        }
    }

    // Deletar role
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarRole(@PathVariable Long id) {
        try {
            boolean deletado = roleService.deletarRole(id);

            if (deletado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao deletar role: " + e.getMessage()));
        }
    }
}
