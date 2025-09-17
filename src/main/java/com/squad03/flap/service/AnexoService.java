package com.squad03.flap.service;

import com.squad03.flap.DTO.CadastroAnexo;
import com.squad03.flap.DTO.BuscaAnexo;
import com.squad03.flap.model.Anexo;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.repository.AnexoRepository;
import com.squad03.flap.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnexoService {

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Transactional
    public BuscaAnexo salvarAnexo(CadastroAnexo dados){
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(dados.tarefaId());

        if(tarefaOptional.isEmpty()){
            throw new IllegalArgumentException("Tarefa não encontrada com o ID: " + dados.tarefaId());
        }

        Tarefa tarefa = tarefaOptional.get();

        Anexo novoAnexo = new Anexo(dados.link(), dados.descricao(), tarefa);

        Anexo anexoSalvo = anexoRepository.save(novoAnexo);

        return new BuscaAnexo(anexoSalvo);
    }

    public List<BuscaAnexo> listarTodos(){
        return anexoRepository.findAll()
                .stream()
                .map(BuscaAnexo::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarAnexo(Long id){
        anexoRepository.deleteById(id);
    }
}
