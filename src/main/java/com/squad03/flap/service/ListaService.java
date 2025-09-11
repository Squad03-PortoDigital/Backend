package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoLista;
import com.squad03.flap.DTO.CadastroLista;
import com.squad03.flap.DTO.BuscaLista;
import com.squad03.flap.model.Lista;
import com.squad03.flap.repository.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListaService {

    private ListaRepository repository;

    @Autowired
    public ListaService(ListaRepository repository) {
        this.repository = repository;
    }

    public void cadastrarLista(CadastroLista cadastroLista) {
        repository.save(new Lista(cadastroLista));
    }

    public Lista buscarListaPorId(int id) {
        return repository.findById(id).get();
    }

    public List<BuscaLista> buscarListas() {
        return repository.findAll().stream().map(BuscaLista::new).collect(Collectors.toList());
    }

    public void AtualizarLista(int id, AtualizacaoLista atualizacaoLista) {
        Lista Lista = repository.findById(id).get();

        Lista.setNome(atualizacaoLista.nome());
        Lista.setPosicao(atualizacaoLista.posicao());
        Lista.setCor(atualizacaoLista.cor());

        repository.save(Lista);
    }

    public void excluirLista(int id) {
        repository.deleteById(id);
    }
}
