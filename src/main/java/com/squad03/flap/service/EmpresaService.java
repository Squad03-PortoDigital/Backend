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

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Autowired
    private DropboxService dropboxService;

    @Transactional
    public BuscaEmpresa cadastrarEmpresa(CadastroEmpresa cadastroEmpresa) {
        Empresa novaEmpresa = new Empresa(cadastroEmpresa);
        Empresa empresaSalva = empresaRepository.save(novaEmpresa);

        try {
            String pastaPath = "/" + sanitizeName(empresaSalva.getNome());
            dropboxService.createFolder(pastaPath);
            System.out.println("📁 Pasta criada no Dropbox: " + pastaPath);
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao criar pasta no Dropbox para empresa: " + e.getMessage());
            // Não falha a criação da empresa se der erro no Dropbox
        }

        return new BuscaEmpresa(empresaSalva);
    }

    public Optional<BuscaEmpresa> buscarEmpresaPorId(Long id) {
        return empresaRepository.findById(id).map(BuscaEmpresa::new);
    }

    public List<BuscaEmpresa> buscarEmpresas() {
        return empresaRepository.findAll()
                .stream()
                .map(BuscaEmpresa::new)
                .collect(Collectors.toList());
    }

    // ✅ NOVO MÉTODO - Buscar por status de arquivamento
    public List<BuscaEmpresa> buscarEmpresasPorStatus(Boolean arquivada) {
        return empresaRepository.findByArquivada(arquivada)
                .stream()
                .map(BuscaEmpresa::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public BuscaEmpresa AtualizarEmpresa(Long id, AtualizacaoEmpresa atualizacaoEmpresa) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        empresa.setNome(atualizacaoEmpresa.nome());
        empresa.setCnpj(atualizacaoEmpresa.cnpj());
        empresa.setEmail(atualizacaoEmpresa.email());
        empresa.setContato(atualizacaoEmpresa.contato());
        empresa.setAtuacao(atualizacaoEmpresa.atuacao());
        empresa.setObservacao(atualizacaoEmpresa.observacao());
        empresa.setFoto(atualizacaoEmpresa.foto());
        empresa.setAgenteLink(atualizacaoEmpresa.agenteLink());

        Empresa empresaAtualizada = empresaRepository.save(empresa);

        return new BuscaEmpresa(empresaAtualizada);
    }

    // ✅ NOVO MÉTODO - Arquivar/Desarquivar empresa
    @Transactional
    public BuscaEmpresa arquivarEmpresa(Long id, Boolean arquivada) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        empresa.setArquivada(arquivada);
        Empresa empresaAtualizada = empresaRepository.save(empresa);

        return new BuscaEmpresa(empresaAtualizada);
    }

    private String sanitizeName(String name) {
        if (name == null || name.isEmpty()) {
            return "Sem-Nome";
        }

        return name
                .trim()
                .replaceAll("[^a-zA-Z0-9\\sÀ-ÿ-]", "") // Remove caracteres especiais, mantém acentos
                .replaceAll("\\s+", "-") // Substitui espaços por hífens
                .replaceAll("-+", "-") // Remove hífens duplicados
                .substring(0, Math.min(name.length(), 50)); // Limita a 50 caracteres
    }

    @Transactional
    public void excluirEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}
