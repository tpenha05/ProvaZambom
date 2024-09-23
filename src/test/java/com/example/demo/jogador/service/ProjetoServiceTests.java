package com.example.demo.jogador.service;

import com.example.demo.jogador.Model.Projeto;
import com.example.demo.jogador.Repository.ProjetoRepository;
import com.example.demo.jogador.Service.ProjetoService;
import com.example.demo.cpf.RetornarCpfDTO;
import com.example.demo.cpf.CpfSerivce;
import com.example.demo.cpf.CpfNaoEncontrandoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTests {


    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private CpfSerivce cpfSerivce;



    @Test
    public void TestCadastrarProjeto(){

        Projeto projeto = new Projeto();
        projeto.setNome("Projeto Alpha");
        projeto.setStatus("PLANEJAMENTO");

        ArrayList<String> lista = new ArrayList<>();
        lista.add("123");
        projeto.setCpf(lista);

        Mockito.when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);

        Projeto retorno = projetoService.salvar(projeto);

        // Verificar os resultados
        Assertions.assertNotNull(retorno);
        Assertions.assertEquals("PLANEJAMENTO", retorno.getStatus());
        Assertions.assertEquals("Projeto Alpha", retorno.getNome());
        Assertions.assertEquals(lista, retorno.getCpf());

    }

    @Test
    public void testListarProjetos() {

        // preparo
        Projeto projeto = new Projeto();
        projeto.setNome("Projeto Beta");
        projeto.setStatus("EXECUCAO ");

        ArrayList<String> lista = new ArrayList<>();
        lista.add("123");
        projeto.setCpf(lista);

        List<Projeto> projetos = new ArrayList<>();
        projetos.add(projeto);

        Mockito.when(projetoRepository.findAll()).thenReturn(projetos);

        // chamada do código testado
        List<Projeto> resultado = projetoService.listar();

        // verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Projeto Beta", resultado.get(0).getNome());
        Assertions.assertEquals("EXECUCAO ", resultado.get(0).getStatus());
        Assertions.assertEquals(lista, resultado.get(0).getCpf());
    }

    @Test
    public void testListarProjetosPorStatus() {
        // preparo
        Projeto projeto1 = new Projeto();
        projeto1.setNome("Projeto Gamma");
        projeto1.setStatus("FINALIZADO");

        Projeto projeto2 = new Projeto();
        projeto2.setNome("Projeto Delta");
        projeto2.setStatus("EXECUCAO ");

        List<Projeto> projetos = new ArrayList<>();
        projetos.add(projeto1);
        projetos.add(projeto2);

        Mockito.when(projetoRepository.findByStatus("FINALIZADO")).thenReturn(List.of(projeto1));

        // chamada do código testado
        List<Projeto> resultado = projetoService.listarPorStatus("FINALIZADO");

        // verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Projeto Gamma", resultado.get(0).getNome());
        Assertions.assertEquals("FINALIZADO", resultado.get(0).getStatus());
    }

    @Test
    public void testListarProjetosPorStatusComStatusNulo() {
        // preparo
        Projeto projeto1 = new Projeto();
        projeto1.setNome("Projeto Epsilon");
        projeto1.setStatus("PLANEJAMENTO");

        Projeto projeto2 = new Projeto();
        projeto2.setNome("Projeto Zeta");
        projeto2.setStatus("EXECUCAO ");

        List<Projeto> projetos = new ArrayList<>();
        projetos.add(projeto1);
        projetos.add(projeto2);

        Mockito.when(projetoRepository.findAll()).thenReturn(projetos);

        // chamada do código testado com status nulo
        List<Projeto> resultado = projetoService.listarPorStatus(null);

        // verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    public void testAdicionarPessoaAoProjeto_Sucesso() {
        // preparo
        String idProjeto = "1";
        String cpf = "123";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);
        projeto.setNome("Projeto Theta");
        projeto.setStatus("EXECUCAO ");

        ArrayList<String> listaPessoas = new ArrayList<>();
        listaPessoas.add("90210");
        projeto.setCpf(listaPessoas);

        RetornarCpfDTO cpfDTO = new RetornarCpfDTO();
        cpfDTO.setCpf(cpf);

        ResponseEntity<RetornarCpfDTO> responseCpf = new ResponseEntity<>(cpfDTO, HttpStatus.OK);

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        Mockito.when(cpfSerivce.getGerente(cpf)).thenReturn(responseCpf);
        Mockito.when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);

        // chamada do código testado
        Projeto resultado = projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);

        // verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.getCpf().contains(cpf));
    }

    @Test
    public void testAdicionarPessoaAoProjeto_ProjetoNaoEncontrado() {
        // preparo
        String idProjeto = "2";
        String cpf = "123";

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.empty());

        // chamada do código testado e verificação da exceção
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);
        });

        Assertions.assertEquals("Projeto não encontrado", exception.getMessage());
    }

    @Test
    public void testAdicionarPessoaAoProjeto_ProjetoFinalizado() {
        // preparo
        String idProjeto = "3";
        String cpf = "123";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);
        projeto.setNome("Projeto Iota");
        projeto.setStatus("FINALIZADO");

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));

        // chamada do código testado e verificação da exceção
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);
        });

        Assertions.assertEquals("Não é possível adicionar pessoas a um projeto finalizado", exception.getMessage());
    }

    @Test
    public void testAdicionarPessoaAoProjeto_CpfNaoEncontrado() {
        // preparo
        String idProjeto = "4";
        String cpf = "123";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);
        projeto.setNome("Projeto Kappa");
        projeto.setStatus("EXECUCAO ");

        ArrayList<String> listaPessoas = new ArrayList<>();
        listaPessoas.add("CPF555555555");
        projeto.setCpf(listaPessoas);

        ResponseEntity<RetornarCpfDTO> responseCpf = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        Mockito.when(cpfSerivce.getGerente(cpf)).thenReturn(responseCpf);

        // chamada do código testado e verificação da exceção
        CpfNaoEncontrandoException exception = Assertions.assertThrows(CpfNaoEncontrandoException.class, () -> {
            projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);
        });

        Assertions.assertEquals("Pessoa não encontrada com o CPF informado", exception.getMessage());
    }

    @Test
    public void testAdicionarPessoaAoProjeto_PessoaJaNoProjeto() {
        // preparo
        String idProjeto = "5";
        String cpf = "123";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);
        projeto.setNome("Projeto Lambda");
        projeto.setStatus("EXECUCAO ");

        ArrayList<String> listaPessoas = new ArrayList<>();
        listaPessoas.add(cpf); // Pessoa já no projeto
        projeto.setCpf(listaPessoas);

        RetornarCpfDTO cpfDTO = new RetornarCpfDTO();
        cpfDTO.setCpf(cpf);

        ResponseEntity<RetornarCpfDTO> responseCpf = new ResponseEntity<>(cpfDTO, HttpStatus.OK);

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        Mockito.when(cpfSerivce.getGerente(cpf)).thenReturn(responseCpf);

        // chamada do código testado e verificação da exceção
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto(idProjeto, cpf);
        });

        Assertions.assertEquals("Pessoa já faz parte do projeto", exception.getMessage());
    }

    @Test
    public void testAdicionarTimeAoProjeto_Sucesso() {
        // Preparação
        String idProjeto = "1";
        String cpf = "123";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);
        projeto.setNome("Projeto Alpha");
        projeto.setStatus("EXECUCAO ");

        ArrayList<String> listaTimes = new ArrayList<>();
        projeto.setCpf(listaTimes); // Lista de times inicialmente vazia

        RetornarCpfDTO cpfDTO = new RetornarCpfDTO();
        cpfDTO.setCpf(cpf);

        ResponseEntity<RetornarCpfDTO> responseTime = new ResponseEntity<>(cpfDTO, HttpStatus.OK);

        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        Mockito.when(cpfSerivce.getGerente(cpf)).thenReturn(responseTime);
        Mockito.when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);

        // Chamada do método
        Projeto resultado = projetoService.addTime(projeto, cpf);

        // Verificações
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.getCpf().contains(cpf));
        Assertions.assertEquals(1, resultado.getCpf().size());
    }

    @Test
    public void testAdicionarTimeAoProjeto_ProjetoNaoEncontrado() {
        // Preparação
        String idProjeto = "2";
        String cpf = "90210";

        Projeto projeto = new Projeto();
        projeto.setId(idProjeto);

        // Simulando projeto não encontrado
        Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(Optional.empty());

        // Verificação de exceção
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.addTime(projeto, cpf);
        });

        Assertions.assertEquals("Projeto não encontrado", exception.getMessage());
    }

}
