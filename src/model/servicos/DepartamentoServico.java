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

}
