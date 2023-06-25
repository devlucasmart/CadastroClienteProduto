package br.com.springboot.dao;

import br.com.springboot.model.Cliente;
import br.com.springboot.model.Fornecedor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository
public class FornecedorDAO implements CRUD<Fornecedor, Long>{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Fornecedor pesquisaPeloId(Long id) {
		return entityManager.find(Fornecedor.class, id);
	}

	@Override
	public List<Fornecedor> lista() {
		Query query = entityManager.createQuery("Select f from Fornecedor f");
		return (List<Fornecedor>) query.getResultList();
	}

	@Override
	public void insere(Fornecedor fornecedor) {
		entityManager.persist(fornecedor);
	}

	@Override
	public void atualiza(Fornecedor fornecedor) {
		entityManager.merge(fornecedor);
	}

	@Override
	public void remove(Fornecedor fornecedor) {
		entityManager.remove(fornecedor);
	}
}
