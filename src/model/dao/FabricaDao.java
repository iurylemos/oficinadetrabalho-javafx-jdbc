package model.dao;

import db.DB;
import model.dao.impl.DepartamentoDaoJDBC;
import model.dao.impl.VendedorDaoJDBC;

public class FabricaDao {
	
	/*
	 * Essa classe auxiliar vai ser respons�vel por
	 * instanciar os meus Daos.
	 * 
	 * Ela vai ter opera��es est�ticas para instanciar os DAO
	 */

	/**
	 * Dessa forma a minha classe vai expor um metodo que retorna
	 * o tipo da interface, mas internamente ela vai instanciar
	 * uma implementa��o.
	 * 
	 * isso aqui � um macete para n�o precisar expor
	 * a implementa��o, deixa s� a interface mesmo.
	 * 
	 * E dentro da instancia tenho que puxar a conex�o com o Banco
	 * 
	 */
	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC(DB.getConnection());
	}
	
	public static DepartamentoDao criarDepartamentoDao() {
		return new DepartamentoDaoJDBC(DB.getConnection());
	}
	
}
