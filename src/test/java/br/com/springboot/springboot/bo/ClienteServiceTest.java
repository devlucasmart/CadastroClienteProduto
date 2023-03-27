package br.com.springboot.springboot.bo;

import br.com.springboot.services.ClienteService;
import br.com.springboot.dao.ClienteDAO;
import br.com.springboot.model.Cliente;
import br.com.springboot.model.Sexo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = ClienteService.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteServiceTest {
    @MockBean
    private ClienteDAO dao;
    @Autowired
    private ClienteService clienteService;
    @Test
    @Order(1)
    public void insere() {
        Cliente cliente = new Cliente();
        cliente.setNome("José da Silva");
        cliente.setCpf("01234567890");
        cliente.setDataDeNascimmento(LocalDate.of(200, 1, 8));
        cliente.getSexo(Sexo.MASCULINO);
        cliente.setTelefone("0123456789");
        cliente.setCelular("01234567890");
        cliente.setAtivo(true);
        clienteService.insere(cliente);

    }
   @Test
    @Order(2)
    public void pesquisaPeloId() {
        Cliente cliente = clienteService.pesquisaPeloId(1L);
        System.out.println(cliente);
    }
   @Test
    @Order(3)
    public void atualiza(){
        Cliente cliente = clienteService.pesquisaPeloId(3L);
        cliente.setNome(null);
        cliente.setCpf(null);
        cliente.setDataDeNascimmento(null);
        cliente.getSexo(null);
        cliente.setTelefone(null);
        cliente.setCelular(null);
        clienteService.atualiza(cliente);
    }
    @Test
    @Order(4)
    public void lista() {
        List<Cliente> clientes = clienteService.lista();
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }
    @Test
    @Order(5)
    public void inativa(Cliente cliente) {
        cliente.setAtivo(false);
        clienteService.atualiza(cliente);
    }

    @Test
    @Order(6)
    public void remove(Cliente cliente) {
        clienteService.remove(cliente);
    }

}
