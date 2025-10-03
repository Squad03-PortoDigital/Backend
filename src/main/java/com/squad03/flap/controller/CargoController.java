package com.squad03.flap.controller;

import com.squad03.flap.model.Cargo;
import com.squad03.flap.DTO.CargoDTO;
import com.squad03.flap.DTO.ContadorDTO;
import com.squad03.flap.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    // Criar novo cargo
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CargoDTO cargoDTO) {
        try {
            Cargo cargo = cargoDTO.toEntity();
            Cargo cargoSalvo = cargoService.salvar(cargo);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CargoDTO(cargoSalvo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar todos os cargos
    @GetMapping
    public ResponseEntity<List<CargoDTO>> buscarTodos() {
        try {
            List<Cargo> cargos = cargoService.buscarTodos();
            List<CargoDTO> cargosDTO = cargos.stream()
                    .map(CargoDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cargosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar cargo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Cargo> cargo = cargoService.buscarPorId(id);
            if (cargo.isPresent()) {
                return ResponseEntity.ok(new CargoDTO(cargo.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Atualizar cargo
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody CargoDTO cargoDTO) {
        try {
            cargoDTO.setId(id);
            Cargo cargo = cargoDTO.toEntity();
            Cargo cargoAtualizado = cargoService.atualizar(cargo);
            return ResponseEntity.ok(new CargoDTO(cargoAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Deletar cargo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            cargoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Buscar cargos por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<CargoDTO>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Cargo> cargos = cargoService.buscarPorNomeContendo(nome);
            List<CargoDTO> cargosDTO = cargos.stream()
                    .map(CargoDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cargosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar cargos com usuários (retorna entidade completa para visualizar usuários)
    @GetMapping("/com-usuarios")
    public ResponseEntity<List<Cargo>> buscarComUsuarios() {
        try {
            List<Cargo> cargos = cargoService.buscarComUsuarios();
            return ResponseEntity.ok(cargos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Contar usuários do cargo
    @GetMapping("/{id}/contar-usuarios")
    public ResponseEntity<?> contarUsuarios(@PathVariable Long id) {
        try {
            Long quantidade = cargoService.contarUsuarios(id);
            return ResponseEntity.ok(new ContadorDTO(quantidade, "usuarios"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }
}