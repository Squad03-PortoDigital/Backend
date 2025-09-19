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

    private ListaRepository repository;

    @Autowired
    public ListaService(ListaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BuscaLista cadastrarLista(CadastroLista cadastroLista) {
        Lista listaNova = repository.save(new Lista(cadastroLista));
        return new BuscaLista(listaNova);
    }

    public Optional<BuscaLista> buscarListaPorId(Long id) {
        return repository.findById(id).map(BuscaLista::new);
    }

    public List<BuscaLista> buscarListas() {
        return repository.findAll().stream().map(BuscaLista::new).collect(Collectors.toList());
    }

    @Transactional
    public BuscaLista AtualizarLista(Long id, AtualizacaoLista atualizacaoLista) {
        Lista Lista = repository.findById(id).get();

        Lista.setNome(atualizacaoLista.nome());
        Lista.setPosicao(atualizacaoLista.posicao());
        Lista.setCor(atualizacaoLista.cor());

        Lista novaLista = repository.save(Lista);

        return new BuscaLista(novaLista);
    }

    @Transactional
    public void excluirLista(Long id) {
        repository.deleteById(id);
    }
}
