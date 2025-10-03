package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoMovimentacao;
import com.squad03.flap.DTO.BuscaMovimentacao;
import com.squad03.flap.DTO.CadastroMovimentacao;
import com.squad03.flap.model.Lista;
import com.squad03.flap.model.Movimentacao;
import com.squad03.flap.model.Role;
import com.squad03.flap.repository.ListaRepository;
import com.squad03.flap.repository.MovimentacaoRepository;
import com.squad03.flap.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimentacaoService {

    private MovimentacaoRepository repository;

    private RoleRepository roleRepository;

    private ListaRepository listaRepository;

    @Autowired
    public MovimentacaoService(MovimentacaoRepository repository, RoleRepository roleRepository, ListaRepository listaRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.listaRepository = listaRepository;
    }

    @Transactional
    public BuscaMovimentacao cadastrarMovimentacao(CadastroMovimentacao dados) {
        Role role = roleRepository.findById(dados.role_id()).orElseThrow();
        Lista listaOrigem = listaRepository.findById(dados.listaOrigem_id()).orElseThrow();
        Lista listaDestino = listaRepository.findById(dados.listaDestino_id()).orElseThrow();

        Movimentacao movimentacao = new Movimentacao(role, listaOrigem, listaDestino);
        return new BuscaMovimentacao(repository.save(movimentacao));
    }

    public Optional<BuscaMovimentacao> buscarMovimentacaoPorId(Long id) {
        return repository.findById(id).map(BuscaMovimentacao::new);
    }

    public List<BuscaMovimentacao> buscarMovimentacoes() {
        return repository.findAll().stream().map(BuscaMovimentacao::new).collect(Collectors.toList());
    }

    @Transactional
    public BuscaMovimentacao atualizarMovimentacao(Long id, AtualizacaoMovimentacao atualizacaoMovimentacao) {
        Movimentacao movimentacao = repository.findById(id).get();

        movimentacao.setRole(roleRepository.findById(atualizacaoMovimentacao.role_id()).orElseThrow());
        movimentacao.setListaOrigem(listaRepository.findById(atualizacaoMovimentacao.listaOrigem_id()).orElseThrow());
        movimentacao.setListaDestino(listaRepository.findById(atualizacaoMovimentacao.listaDestino_id()).orElseThrow());
        return new BuscaMovimentacao(repository.save(movimentacao));
    }

    @Transactional
    public void deletarMovimentacao(Long id) {
        Movimentacao movimentacao = repository.findById(id).get();
        repository.deleteById(movimentacao.getId());
    }
}
