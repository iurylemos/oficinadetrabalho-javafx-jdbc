package model.servicos;

import java.util.ArrayList;
import java.util.List;

import model.entidades.Departamento;

public class DepartamentoServico {
	
	/*
	 * Vou lá no DepartamentoListController declarar uma depedência para cá.
	 */
	
	public List<Departamento> findAll() {
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Books"));
		list.add(new Departamento(2, "Computadores"));
		list.add(new Departamento(3, "Eletronicos"));
		return list;
	}

}
