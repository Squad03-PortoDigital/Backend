package com.squad03.flap.controller;

import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.DTO.UsuarioDTO;
import com.squad03.flap.DTO.UsuarioResponseDTO;
import com.squad03.flap.DTO.FotoDTO;
import com.squad03.flap.model.Role;
import java.util.Map;

import com.squad03.flap.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    // ==================== ENDPOINTS DE AUTENTICAÇÃO ====================

    // Endpoint para obter usuário autenticado (login do front)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);

            if (usuario.isPresent()) {
                return ResponseEntity.ok(new UsuarioResponseDTO(usuario.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // ✅ ADICIONE este método no UsuarioController
    @PutMapping("/me")
    public ResponseEntity<?> atualizarMeuPerfil(
            Authentication authentication,
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            // ✅ Permite editar nome e foto
            if (usuarioDTO.getNome() != null && !usuarioDTO.getNome().isBlank()) {
                usuario.setNome(usuarioDTO.getNome());
            }
            if (usuarioDTO.getFoto() != null) {
                usuario.setFoto(usuarioDTO.getFoto());
            }

            Usuario atualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(new UsuarioResponseDTO(atualizado));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar perfil: " + e.getMessage());
        }
    }


    // ==================== CRUD DE USUÁRIOS ====================

    @PostMapping("/cadastro")
    public ResponseEntity<?> criar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Validações básicas
            if (usuarioDTO.getNome() == null || usuarioDTO.getNome().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Nome é obrigatório"));
            }

            if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "E-mail é obrigatório"));
            }

            if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Senha é obrigatória"));
            }

            // Verifica se o email já existe
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "E-mail já cadastrado"));
            }

            Usuario usuario = usuarioDTO.toEntity();

            // ✅ Define role padrão se não foi informada
            if (usuario.getRole() == null || usuario.getRole().getId() == null) {
                Role roleDefault = new Role();
                roleDefault.setId(1L); // ID da role padrão (usuário comum)
                usuario.setRole(roleDefault);
            }

            Usuario usuarioSalvo = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UsuarioResponseDTO(usuarioSalvo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Erro de validação: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Para debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro interno: " + e.getMessage()));
        }
    }


    // Buscar todos os usuários
    @GetMapping
    @PreAuthorize("hasAuthority('USUARIO_LER')")
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
    @PreAuthorize("hasAuthority('USUARIO_LER')")
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

    // ✅ ATUALIZAR USUÁRIO (PUT) - SEM BIO
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_EDITAR_PERMISSAO')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioExistente = usuarioService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            // ✅ Atualiza nome e foto
            if (usuarioDTO.getNome() != null && !usuarioDTO.getNome().isBlank()) {
                usuarioExistente.setNome(usuarioDTO.getNome());
            }
            if (usuarioDTO.getFoto() != null) {
                usuarioExistente.setFoto(usuarioDTO.getFoto());
            }

            Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }


    // Deletar usuário
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_EDITAR_PERMISSAO')")
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

    // ==================== ENDPOINTS DE BUSCA ====================

    // Buscar usuário por email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('USUARIO_LER')")
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
    @PreAuthorize("hasAuthority('USUARIO_LER')")
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

    // Atualizar foto
    @PutMapping("/{id}/foto")
    @PreAuthorize("hasAuthority('USUARIO_EDITAR_PERMISSAO')") // Exige permissão de edição
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
