package com.example.demo.jogador.Controller;


import com.example.demo.jogador.Model.Projeto;
import com.example.demo.jogador.Service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogador")
public class ProjetoController {


    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public List<Projeto> listar(@RequestParam(value = "status", required = false) String status) {
        return projetoService.listarPorStatus(status);
    }
    @PostMapping
    public Projeto salvar(@RequestBody Projeto projeto) {
        return projetoService.salvar(projeto);
    }

    @PostMapping("/{idTime}")
    public Projeto salvarComCpf(@RequestBody Projeto projeto, @PathVariable String cpf) {
        return projetoService.addTime(projeto, cpf);
    }

    @PostMapping("/{idProjeto}/adicionarPessoa")
    public Projeto adicionarPessoa(@PathVariable String idProjeto, @RequestParam String cpf) {
        return projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);
    }

}