package model.servicos;

import java.util.List;

import model.dao.DepartamentoDao;
import model.dao.FabricaDao;
import model.entidades.Departamento;

public class DepartamentoServico {
	
	//Depedencia
	private DepartamentoDao dao = FabricaDao.criarDepartamentoDao();
	
	
	/*
	 * Vou lá no DepartamentoListController declarar uma depedência para cá.
	 */
	
	public List<Departamento> findAll() {
		
		return dao.findAll();
		
		
		/*
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Books"));
		list.add(new Departamento(2, "Computadores"));
		list.add(new Departamento(3, "Eletronicos"));
		return list; 
		Troca essa implementação mocada (ENFEITE) */
	}
	
	public void salvarOuAtualizar(Departamento obj) {
		/*
		 * Vou fazer o que com o objeto que está vindo no parametro?
		 * Vou ter que ver se eu tenho que inserir um novo Departamento
		 * ou atualizar o Departamento existe.
		 * vou testar se esse objeto ele tem um ID igual a nulo.
		 */
		if(obj.getId() == null) {
			//Significa que estou inserindo um novo departamento
			dao.insert(obj);
		}else {
			//significa que já existe
			dao.update(obj);
		}
	}
	
	public void remover(Departamento obj) {
		//remover um departamento do banco de dados.
		dao.deleteById(obj.getId());
	}

}
