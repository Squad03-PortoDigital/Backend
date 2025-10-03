package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoLista;
import com.squad03.flap.DTO.BuscaEmpresa;
import com.squad03.flap.DTO.CadastroLista;
import com.squad03.flap.DTO.BuscaLista;
import com.squad03.flap.model.Lista;
import com.squad03.flap.repository.ListaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListaService {

    @Autowired
    private ListaRepository repository;

    @Transactional
    public BuscaLista cadastrarLista(CadastroLista cadastroLista) {
        Lista listaNova = new Lista(cadastroLista);
        Lista listaSalva = repository.save(listaNova);
        return new BuscaLista(listaSalva);
    }

    public Optional<BuscaLista> buscarListaPorId(Long id) {
        return repository.findById(id).map(BuscaLista::new);
    }

    public List<BuscaLista> buscarListas() {
        return repository.findAll().stream().map(BuscaLista::new).collect(Collectors.toList());
    }

    @Transactional
    public Optional<BuscaLista> atualizarLista(Long id, AtualizacaoLista atualizacaoLista) {
        return repository.findById(id)
                .map(lista -> {

                    if (atualizacaoLista.nome() != null) {
                        lista.setNome(atualizacaoLista.nome());
                    }
                    if (atualizacaoLista.posicao() != 0) {
                        lista.setPosicao(atualizacaoLista.posicao());
                    }
                    if (atualizacaoLista.cor() != null) {
                        lista.setCor(atualizacaoLista.cor());
                    }

                    return new BuscaLista(repository.save(lista));
                });
    }

    @Transactional
    public boolean excluirLista(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
