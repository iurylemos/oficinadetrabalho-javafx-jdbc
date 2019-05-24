package model.dao;

import db.DB;
import model.dao.impl.DepartamentoDaoJDBC;
import model.dao.impl.VendedorDaoJDBC;

public class FabricaDao {
	
	/*
	 * Essa classe auxiliar vai ser responsável por
	 * instanciar os meus Daos.
	 * 
	 * Ela vai ter operações estáticas para instanciar os DAO
	 */

	/**
	 * Dessa forma a minha classe vai expor um metodo que retorna
	 * o tipo da interface, mas internamente ela vai instanciar
	 * uma implementação.
	 * 
	 * isso aqui é um macete para não precisar expor
	 * a implementação, deixa só a interface mesmo.
	 * 
	 * E dentro da instancia tenho que puxar a conexão com o Banco
	 * 
	 */
	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC(DB.getConnection());
	}
	
	public static DepartamentoDao criarDepartamentoDao() {
		return new DepartamentoDaoJDBC(DB.getConnection());
	}
	
}
