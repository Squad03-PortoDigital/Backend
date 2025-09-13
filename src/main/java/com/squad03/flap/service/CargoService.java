package com.squad03.flap.service;


import com.squad03.flap.model.Cargo;
import com.squad03.flap.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    // Salvar novo cargo
    public Cargo salvar(Cargo cargo) {
        if (cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }

        if (cargoRepository.existsByNome(cargo.getNome())) {
            throw new IllegalArgumentException("Já existe um cargo com este nome");
        }

        return cargoRepository.save(cargo);
    }

    // Atualizar cargo
    public Cargo atualizar(Cargo cargo) {
        if (cargo.getId() == null) {
            throw new IllegalArgumentException("ID do cargo não pode ser nulo");
        }

        if (!cargoRepository.existsById(cargo.getId())) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }

        if (cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }

        // Verificar se já existe outro cargo com o mesmo nome
        Optional<Cargo> cargoExistente = cargoRepository.findByNome(cargo.getNome());
        if (cargoExistente.isPresent() && !cargoExistente.get().getId().equals(cargo.getId())) {
            throw new IllegalArgumentException("Já existe um cargo com este nome");
        }

        return cargoRepository.save(cargo);
    }

    // Buscar todos os cargos
    @Transactional(readOnly = true)
    public List<Cargo> buscarTodos() {
        return cargoRepository.findAllByOrderByNomeAsc();
    }

    // Buscar cargo por ID
    @Transactional(readOnly = true)
    public Optional<Cargo> buscarPorId(Long id) {
        return cargoRepository.findById(id);
    }

    // Buscar cargo por nome
    @Transactional(readOnly = true)
    public Optional<Cargo> buscarPorNome(String nome) {
        return cargoRepository.findByNome(nome);
    }

    // Buscar cargos por nome contendo
    @Transactional(readOnly = true)
    public List<Cargo> buscarPorNomeContendo(String nome) {
        return cargoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar cargos com usuários
    @Transactional(readOnly = true)
    public List<Cargo> buscarComUsuarios() {
        return cargoRepository.findAllWithUsuarios();
    }

    // Contar usuários por cargo
    @Transactional(readOnly = true)
    public Long contarUsuarios(Long cargoId) {
        return cargoRepository.countUsuariosByCargoId(cargoId);
    }

    // Deletar cargo
    public void deletar(Long id) {
        if (!cargoRepository.existsById(id)) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }

        Long quantidadeUsuarios = cargoRepository.countUsuariosByCargoId(id);
        if (quantidadeUsuarios > 0) {
            throw new IllegalArgumentException("Não é possível deletar cargo que possui usuários associados");
        }

        cargoRepository.deleteById(id);
    }

    // Verificar se existe
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return cargoRepository.existsById(id);
    }
}