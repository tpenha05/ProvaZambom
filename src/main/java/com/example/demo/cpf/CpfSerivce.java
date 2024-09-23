package com.example.demo.cpf;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CpfSerivce {

    public ResponseEntity<RetornarCpfDTO> getGerente(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(
                "http://184.72.80.215:8080/usuario/" + cpf,
                RetornarCpfDTO.class);
    }
}