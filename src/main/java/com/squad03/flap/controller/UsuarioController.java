package com.squad03.flap.controller;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.DTO.UsuarioDTO;
import com.squad03.flap.DTO.UsuarioResponseDTO;
import com.squad03.flap.DTO.LoginDTO;
import com.squad03.flap.DTO.FotoDTO;
import com.squad03.flap.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // NOVO: Endpoint para obter usuário autenticado (usado no login do front)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String email = authentication.getName(); // Retorna o username/email usado no Basic Auth
            Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);

            if (usuario.isPresent()) {
                return ResponseEntity.ok(new UsuarioResponseDTO(usuario.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Criar novo usuário
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioDTO.toEntity();
            Usuario usuarioSalvo = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(usuarioSalvo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos() {
        try {
            List<Usuario> usuarios = usuarioService.buscarTodos();
            List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                    .map(UsuarioResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(new UsuarioResponseDTO(usuario.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioDTO.setId(id);
            Usuario usuario = usuarioDTO.toEntity();
            Usuario usuarioAtualizado = usuarioService.atualizar(usuario);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar usuário por email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(new UsuarioResponseDTO(usuario.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar usuários por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Usuario> usuarios = usuarioService.buscarPorNome(nome);
            List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                    .map(UsuarioResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar usuários por cargo
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorCargo(@PathVariable Long cargoId) {
        try {
            List<Usuario> usuarios = usuarioService.buscarPorCargo(cargoId);
            List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                    .map(UsuarioResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar usuários por nome do cargo
    @GetMapping("/cargo/nome/{nomeCargo}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNomeCargo(@PathVariable String nomeCargo) {
        try {
            List<Usuario> usuarios = usuarioService.buscarPorNomeCargo(nomeCargo);
            List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                    .map(UsuarioResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Atualizar foto
    @PutMapping("/{id}/foto")
    public ResponseEntity<?> atualizarFoto(@PathVariable Long id, @RequestBody FotoDTO fotoDTO) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizarFoto(id, fotoDTO.getFoto());
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }
}
