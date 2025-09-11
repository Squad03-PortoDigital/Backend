package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoEmpresa;
import com.squad03.flap.DTO.CadastroEmpresa;
import com.squad03.flap.DTO.BuscaEmpresa;
import com.squad03.flap.model.Empresa;
import com.squad03.flap.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public void cadastrarEmpresa(CadastroEmpresa cadastroEmpresa) {
        empresaRepository.save(new Empresa(cadastroEmpresa));
    }

    public Empresa buscarEmpresaPorId(int id) {
        return empresaRepository.findById(id).get();
    }

    public List<BuscaEmpresa> buscarEmpresas() {
        return empresaRepository.findAll().stream().map(BuscaEmpresa::new).collect(Collectors.toList());
    }

    public void AtualizarEmpresa(int id, AtualizacaoEmpresa atualizacaoEmpresa) {
        Empresa empresa = empresaRepository.findById(id).get();

        empresa.setNome(atualizacaoEmpresa.nome());
        empresa.setFoto(atualizacaoEmpresa.foto());

        empresaRepository.save(empresa);
    }

    public void excluirEmpresa(int id) {
        empresaRepository.deleteById(id);
    }
}
