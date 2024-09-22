package com.example.demo.jogador.Repository;


import com.example.demo.jogador.Model.Jogador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JogadorRepository extends MongoRepository<Jogador, String> {
}