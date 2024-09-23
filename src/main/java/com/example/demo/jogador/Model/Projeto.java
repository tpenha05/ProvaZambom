package com.example.demo.jogador.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;



@Document
public class Projeto {

    @Id
    private String id;
    private String nome;
    private String descricao;
    private String status;

    private ArrayList<String> cpf;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String>getCpf() {
        return cpf;
    }

    public void setCpf(ArrayList<String> cpf) {
        this.cpf = cpf;
    }

    public Projeto(){
    }

}