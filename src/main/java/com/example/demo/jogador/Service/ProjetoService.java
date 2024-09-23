package com.example.demo.jogador.Service;


import com.example.demo.jogador.Model.Projeto;
import com.example.demo.jogador.Repository.ProjetoRepository;
import com.example.demo.cpf.CpfNaoEncontrandoException;
import com.example.demo.cpf.RetornarCpfDTO;
import com.example.demo.cpf.CpfSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private CpfSerivce cpfSerivce;


    public Projeto salvar(Projeto projeto){
        return projetoRepository.save(projeto);
    }

    public List<Projeto> listar(){
        return projetoRepository.findAll();
    }

    public List<Projeto> listarPorStatus(String status) {
        if (status == null || status.isEmpty()) {
            return listar();
        }
        return projetoRepository.findByStatus(status);
    }

    public Projeto adicionarPessoaAoProjeto(String idProjeto, String cpf) {
        Optional<Projeto> opProjeto = projetoRepository.findById(String.valueOf(idProjeto));
        if (opProjeto.isEmpty()) {
            throw new RuntimeException("Projeto não encontrado");
        }

        Projeto projeto = opProjeto.get();

        if ("FINALIZADO".equalsIgnoreCase(projeto.getStatus())) {
            throw new RuntimeException("Não é possível adicionar pessoas a um projeto finalizado");
        }

        ResponseEntity<RetornarCpfDTO> responseCpf = cpfSerivce.getGerente(cpf);
        if (responseCpf.getStatusCode().is2xxSuccessful()) {
            RetornarCpfDTO cpfDTO = responseCpf.getBody();
            ArrayList<String> listaPessoas = projeto.getCpf();

            if (!listaPessoas.contains(cpfDTO.getCpf())) {
                listaPessoas.add(cpfDTO.getCpf());
                projeto.setCpf(listaPessoas);
                return projetoRepository.save(projeto);
            } else {
                throw new RuntimeException("Pessoa já faz parte do projeto");
            }
        } else {
            throw new CpfNaoEncontrandoException("Pessoa não encontrada com o CPF informado");
        }
    }

    public Projeto addTime(Projeto projeto, String id){
        Optional<Projeto> op = projetoRepository.findById(projeto.getId());
        if (op.isEmpty()) {
            throw new RuntimeException("Projeto não encontrado");
        }
        Projeto projeto_ = op.get();
        ResponseEntity<RetornarCpfDTO> cpf = cpfSerivce.getGerente(id);
        if(cpf.getStatusCode().is2xxSuccessful()){
            RetornarCpfDTO cpfDTO = cpf.getBody();
            ArrayList<String> lista = projeto_.getCpf();
            lista.add(cpfDTO.getCpf());
            projeto_.setCpf(lista);
            return projetoRepository.save(projeto_);

        }
        else {
            throw new CpfNaoEncontrandoException("Time não encontrado");
        }


    }

}