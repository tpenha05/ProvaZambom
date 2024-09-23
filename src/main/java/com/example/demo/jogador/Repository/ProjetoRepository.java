package com.example.demo.jogador.Repository;


import com.example.demo.jogador.Model.Projeto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProjetoRepository extends MongoRepository<Projeto, String> {
    List<Projeto> findByStatus(String status);
}