package br.com.springboot.services;

import br.com.springboot.dao.CRUD;
import br.com.springboot.dao.ClienteDAO;
import br.com.springboot.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ClienteService implements CRUD <Cliente, Long>{
    @Autowired
    private ClienteDAO dao;

    @Override
    public Cliente pesquisaPeloId(Long id) {
        return dao.pesquisaPeloId(id);
    }

    @Override
    public List<Cliente> lista() {

        return dao.lista();
    }
    @Transactional
    @Override
    public void insere(Cliente cliente) {
        dao.insere(cliente);

    }

    @Transactional
    @Override
    public void atualiza(Cliente cliente) {
        dao.atualiza(cliente);

    }

    @Override
    public void remove(Cliente cliente) {

        dao.remove(cliente);
    }
    @Transactional
    public void inativa(Cliente cliente) {
        cliente.setAtivo(false);
        dao.atualiza(cliente);
    }

    @Transactional
    public void ativa(Cliente cliente){
        cliente.setAtivo(true);
        dao.atualiza(cliente);
    }
}
