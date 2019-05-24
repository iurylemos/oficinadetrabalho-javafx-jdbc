package model.dao;

import java.util.List;

import model.entidades.Departamento;
import model.entidades.Vendedor;

public interface VendedorDao {

	/**
	 * Essa vai ser uma operação responsável
	 * por inserir no banco de dados
	 * esse objeto que eu enviar como parametro
	 */
	void insert(Vendedor obj);
	void update(Vendedor obj);
	void deleteById(Integer id);
	/**
	 * 
	 * Esse operação vai ser responsável por
	 * pegar esse id que vem pelo parametro
	 * e consultar no banco de dados o obj com esse id
	 * se existir vai retornar, se não existir retorna nulo.
	 */
	Vendedor findById(Integer id);
	
	/*
	 * Retornar todo o departamento
	 */
	List<Vendedor> findAll();
	//Vendedor por departamento
	List<Vendedor> findByDepartment(Departamento departamento);
}
