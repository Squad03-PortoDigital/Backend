package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoEmpresa;
import com.squad03.flap.DTO.CadastroEmpresa;
import com.squad03.flap.DTO.BuscaEmpresa;
import com.squad03.flap.model.Empresa;
import com.squad03.flap.repository.EmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public BuscaEmpresa cadastrarEmpresa(CadastroEmpresa cadastroEmpresa) {
        Empresa novaEmpresa = new Empresa(cadastroEmpresa);
        Empresa empresaSalva = empresaRepository.save(novaEmpresa);
        return new BuscaEmpresa(empresaSalva);
    }

    public Optional<BuscaEmpresa> buscarEmpresaPorId(Long id) {
        return empresaRepository.findById(id).map(BuscaEmpresa::new);
    }

    public List<BuscaEmpresa> buscarEmpresas() {
        return empresaRepository.findAll().stream().map(BuscaEmpresa::new).collect(Collectors.toList());
    }

    @Transactional
    public BuscaEmpresa AtualizarEmpresa(Long id, AtualizacaoEmpresa atualizacaoEmpresa) {
        Empresa empresa = empresaRepository.findById(id).get();

        empresa.setNome(atualizacaoEmpresa.nome());
        empresa.setCnpj(atualizacaoEmpresa.cnpj());
        empresa.setEmail(atualizacaoEmpresa.email());
        empresa.setContato(atualizacaoEmpresa.contato());
        empresa.setAtuacao(atualizacaoEmpresa.atuacao());
        empresa.setObservacao(atualizacaoEmpresa.observacao());
        empresa.setFoto(atualizacaoEmpresa.foto());

        Empresa empresaAtualizada = empresaRepository.save(empresa);

        return new BuscaEmpresa(empresaAtualizada);
    }

    @Transactional
    public void excluirEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}
