package br.com.springboot.services;

import br.com.springboot.dao.CRUD;
import br.com.springboot.dao.FornecedorDAO;
import br.com.springboot.model.Fornecedor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FornecedorService implements CRUD<Fornecedor, Long> {

    private final FornecedorDAO dao;
    @Override
    public Fornecedor pesquisaPeloId(Long id) {
        return dao.pesquisaPeloId(id);
    }

    @Override
    public List<Fornecedor> lista() {
        return dao.lista();
    }

    @Override
    public void insere(Fornecedor fornecedor) {
        dao.insere(fornecedor);
    }

    @Override
    public void atualiza(Fornecedor fornecedor) {
        dao.atualiza(fornecedor);
    }

    @Override
    public void remove(Fornecedor fornecedor) {
        dao.remove(fornecedor);
    }

    @Transactional
    public void inativa(Fornecedor fornecedor) {
        fornecedor.setAtivo(false);
        dao.atualiza(fornecedor);
    }

    @Transactional
    public void ativa(Fornecedor fornecedor){
        fornecedor.setAtivo(true);
        dao.atualiza(fornecedor);
    }
}
