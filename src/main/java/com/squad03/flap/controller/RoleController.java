package com.squad03.flap.controller;


import com.squad03.flap.model.Role;
import com.squad03.flap.DTO.RoleDTO;
import com.squad03.flap.DTO.ContadorDTO;
import com.squad03.flap.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Roles", description = "Gerenciamento de roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Criar nova role
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = roleDTO.toEntity();
            Role roleSalva = roleService.salvar(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RoleDTO(roleSalva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar todas as roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>> buscarTodas() {
        try {
            List<Role> roles = roleService.buscarTodas();
            List<RoleDTO> rolesDTO = roles.stream()
                    .map(RoleDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(rolesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar role por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Role> role = roleService.buscarPorId(id);
            if (role.isPresent()) {
                return ResponseEntity.ok(new RoleDTO(role.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Atualizar role
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        try {
            roleDTO.setId(id);
            Role role = roleDTO.toEntity();
            Role roleAtualizada = roleService.atualizar(role);
            return ResponseEntity.ok(new RoleDTO(roleAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Deletar role
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            roleService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar roles por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<RoleDTO>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Role> roles = roleService.buscarPorNomeContendo(nome);
            List<RoleDTO> rolesDTO = roles.stream()
                    .map(RoleDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(rolesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar roles com usuários (retorna entidade completa para visualizar usuários)
    @GetMapping("/com-usuarios")
    public ResponseEntity<List<Role>> buscarComUsuarios() {
        try {
            List<Role> roles = roleService.buscarComUsuarios();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Contar usuários da role
    @GetMapping("/{id}/contar-usuarios")
    public ResponseEntity<?> contarUsuarios(@PathVariable Long id) {
        try {
            Long quantidade = roleService.contarUsuarios(id);
            return ResponseEntity.ok(new ContadorDTO(quantidade, "usuarios"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }
}