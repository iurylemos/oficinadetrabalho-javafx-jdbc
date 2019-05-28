package model.servicos;

import java.util.List;

import model.dao.FabricaDao;
import model.dao.VendedorDao;
import model.entidades.Vendedor;

public class VendedorServico {
	
	private VendedorDao vendedorDao = FabricaDao.criarVendedorDao();
	
	public List<Vendedor> findAll() {
		return vendedorDao.findAll();
	}
	
	public void remover(Vendedor vendedor) {
		vendedorDao.deleteById(vendedor.getId());
	}
	
	public void salvarOuAtualizar(Vendedor obj) {
		/*
		 * Vou fazer o que com o objeto que está vindo no parametro?
		 * Vou ter que ver se eu tenho que inserir um novo Departamento
		 * ou atualizar o Departamento existe.
		 * vou testar se esse objeto ele tem um ID igual a nulo.
		 */
		if(obj.getId() == null) {
			//Significa que estou inserindo um novo departamento
			vendedorDao.insert(obj);
		}else {
			//significa que já existe
			vendedorDao.update(obj);
		}
	}

}
