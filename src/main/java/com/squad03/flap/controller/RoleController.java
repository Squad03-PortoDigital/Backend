package com.squad03.flap.controller;

import com.squad03.flap.DTO.*;
import com.squad03.flap.service.AnexoService;
import com.squad03.flap.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Gerenciamento dos rp;es")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<BuscaRole> salvarRole(@RequestBody CadastroRole dados) {
        BuscaRole roleSalvo = roleService.cadastrarRole(dados);
        return new ResponseEntity<>(roleSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscaRole>> listarRoles() {
        List<BuscaRole> roles = roleService.buscarRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaRole> buscarRolePorId(@PathVariable long id) {
        Optional<BuscaRole> role = roleService.buscaRolePorId(id);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuscaRole> atualizarRole(@PathVariable Long id, @RequestBody AtualizacaoRole dados) {
        return roleService.atualizarRole(id, dados)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRole(@PathVariable Long id) {
        boolean deletado = roleService.deletarRole(id);

        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
