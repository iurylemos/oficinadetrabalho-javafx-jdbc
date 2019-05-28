package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entidades.Departamento;
import model.entidades.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {
	
	//conex�o
	private Connection conn;
	//for�ar a depedencia no construtor.
	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Vendedor obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					//Retornando o ID do novo vendedor inserido.
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			//Para buscar o DepartmentId do funcion�rio
			//Tenho que colocar o obj entrando no Departamento, e partir o departamento eu entro no ID
			//Ou seja tenho que navegar no objeto.
			st.setInt(5, obj.getDepartamento().getId());
			
			//Executando
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				//Vou fazer um if, para inserir mais dados.
				if(rs.next()) {
					//Se existir esse cara, vou pegar o id gerado.
					//Na posi��o 1, pois vai ser a primeira coluna das chaves.
					int id = rs.getInt(1);
					//Atribuir ao obj preenchendo com o novo Id do novo usu�rio.
					obj.setId(id);
				//Fechando o ResultSet
				DB.closeResultSet(rs);
				}else {
					throw new DbException("Erro inesperado! Nenhuma linha afetada");
				}
			}
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
		
				
		
	}

	@Override
	public void update(Vendedor obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+"WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId()); //ID do Departamento
			st.setInt(6, obj.getId()); //Esse � o ID do Vendedor
			
			//Executando
			
			st.executeUpdate();
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM seller "
				+"WHERE Id = ?");
			
			//Configurar o valor do place holder.
			//O 1� ?, e no 2� coloca o que veio por parametro.
			st.setInt(1, id);
			int linhasAfetadas = st.executeUpdate();
			if(linhasAfetadas == 0) {
				throw new DbException("Usu�rio invalido!");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}
	
	/*
	 * Puxando o ID do vendedor no banco
	 */

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null; //Traz os dados na forma de tabela.
		try {
			//Criando a query.
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			//Inserindo o valor para o ?
			//No 1� parametro coloca o ?, e depois o que vai receber no caso � o ID que vem como parametro
			st.setInt(1, id);
			rs = st.executeQuery();
			/*
			 * Como no caso quando vai executar ele come�a na posi��o 0
			 * e no caso o 0 s� tem as colunas, dizendo nome email e etc
			 * ai no caso eu vou colocar o next para ele pular a linha
			 * 
			 * SE a CONSULTA gerou algum resultado?
			 * Beleza, ele vai retornar o resultado por esse id
			 * se n�o ele vai retornar falso ou seja nulo
			 * n�o existe o id procurado
			 */
			if(rs.next()) {
				//Instanciando o departamento
				Departamento dep = instanciandoDepartamento(rs);
				//Inserindo no vendedor.
				Vendedor obj = instanciandoVendedor(rs, dep);
				/*
				 * Retirei o c�digo enorme que estava aqui
				 * e criei os metodos auxiliares.
				 */
				return obj;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	/***************************************************/
	/********* METODOS AUXILIARES *********************/
	private Vendedor instanciandoVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalarioBase(rs.getDouble("BaseSalary"));
		obj.setDataNascimento(rs.getDate("BirthDate"));
		obj.setDepartamento(dep);
		return obj;
	}

	private Departamento instanciandoDepartamento(ResultSet rs) throws SQLException {
		/*
		 * N�o vou tratar aqui, pois quando esse metodo �
		 * utilizado, j� est� sendo tratado l�
		 * ent�o s� vou utilizar o trows
		 * ou seja vou propagar
		 */
		 Departamento dep = new Departamento();
			//Setando os valores dele
			dep.setId(rs.getInt("DepartmentId"));
			dep.setNome(rs.getString("DepName"));
			return dep;
	}
	
	/*********************************************/
	
	

	@Override
	public List<Vendedor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null; //Traz os dados na forma de tabela.
		try {
			//Criando a query.
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Vendedor> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
	
			while(rs.next()) {
				
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instanciandoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Vendedor obj = instanciandoVendedor(rs, dep);
				list.add(obj);
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> findByDepartment(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null; //Traz os dados na forma de tabela.
		try {
			//Criando a query.
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			//Inserindo o valor para o ?
			//No 1� parametro coloca o Id, e como ele � inteiro.
			//No 2� vai ser o departamento que est� no parametro
			st.setInt(1, departamento.getId());
			
			rs = st.executeQuery();
			
			//Como s�o v�rios valores, vou utilizar uma Lista.
			
			List<Vendedor> list = new ArrayList<>();
			//Utilizando Map, para que seja um departamento s�, e n�o dois.
			Map<Integer, Departamento> map = new HashMap<>();
			
			
			//O meu resultado pode ter 0 ou mais valores.
			//Vou botar o while, para ele pecorrer, enquanto tiver um pr�ximo.
			//Pois pode ser mais resultados.
			while(rs.next()) {
				/*
				 * Criei um map v�zio.
				 * E agora vou guardar dentro desse map
				 * qualquer departamento que eu instanciar.
				 * E cada vez que passar pelo while
				 * Ele vai testar esse departamento j� existe
				 * E ai ele vem aqui no MAP pego com o GET
				 * um departamento que tem esse ID
				 * que � o DepartmentId
				 * Se n�o existir, esse map.get vai retornar nulo
				 * e se for nulo ai sim vai instanciar o departamento
				 * 
				 */
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				/*
				 * Ai no caso se existir, vou reaproveitar o
				 * departamento que j� existia. 
				 * Se n�o ele retorna nulo, e vai cair no if abaixo.
				 */
				if(dep == null) {
					dep = instanciandoDepartamento(rs);
					/* guardando no map
					 * O primeiro parametro �
					 * o valor da chave que � o nome do Campo.
					 * e o departamento que � o pr�prio dep. */
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//Instancia��o abaixo est� sendo feita dentro do IF
				//Departamento dep = instanciandoDepartamento(rs);
				//Inserindo no vendedor.
				Vendedor obj = instanciandoVendedor(rs, dep);
				//Adicionando os vendedores a lista.
				list.add(obj);
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
