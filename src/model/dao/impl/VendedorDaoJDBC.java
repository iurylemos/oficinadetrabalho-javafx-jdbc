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
	
	//conexão
	private Connection conn;
	//forçar a depedencia no construtor.
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
			//Para buscar o DepartmentId do funcionário
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
					//Na posição 1, pois vai ser a primeira coluna das chaves.
					int id = rs.getInt(1);
					//Atribuir ao obj preenchendo com o novo Id do novo usuário.
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
			st.setInt(6, obj.getId()); //Esse é o ID do Vendedor
			
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
			//O 1º ?, e no 2º coloca o que veio por parametro.
			st.setInt(1, id);
			int linhasAfetadas = st.executeUpdate();
			if(linhasAfetadas == 0) {
				throw new DbException("Usuário invalido!");
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
			//No 1º parametro coloca o ?, e depois o que vai receber no caso é o ID que vem como parametro
			st.setInt(1, id);
			rs = st.executeQuery();
			/*
			 * Como no caso quando vai executar ele começa na posição 0
			 * e no caso o 0 só tem as colunas, dizendo nome email e etc
			 * ai no caso eu vou colocar o next para ele pular a linha
			 * 
			 * SE a CONSULTA gerou algum resultado?
			 * Beleza, ele vai retornar o resultado por esse id
			 * se não ele vai retornar falso ou seja nulo
			 * não existe o id procurado
			 */
			if(rs.next()) {
				//Instanciando o departamento
				Departamento dep = instanciandoDepartamento(rs);
				//Inserindo no vendedor.
				Vendedor obj = instanciandoVendedor(rs, dep);
				/*
				 * Retirei o código enorme que estava aqui
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
		 * Não vou tratar aqui, pois quando esse metodo é
		 * utilizado, já está sendo tratado lá
		 * então só vou utilizar o trows
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
			//No 1º parametro coloca o Id, e como ele é inteiro.
			//No 2º vai ser o departamento que está no parametro
			st.setInt(1, departamento.getId());
			
			rs = st.executeQuery();
			
			//Como são vários valores, vou utilizar uma Lista.
			
			List<Vendedor> list = new ArrayList<>();
			//Utilizando Map, para que seja um departamento só, e não dois.
			Map<Integer, Departamento> map = new HashMap<>();
			
			
			//O meu resultado pode ter 0 ou mais valores.
			//Vou botar o while, para ele pecorrer, enquanto tiver um próximo.
			//Pois pode ser mais resultados.
			while(rs.next()) {
				/*
				 * Criei um map vázio.
				 * E agora vou guardar dentro desse map
				 * qualquer departamento que eu instanciar.
				 * E cada vez que passar pelo while
				 * Ele vai testar esse departamento já existe
				 * E ai ele vem aqui no MAP pego com o GET
				 * um departamento que tem esse ID
				 * que é o DepartmentId
				 * Se não existir, esse map.get vai retornar nulo
				 * e se for nulo ai sim vai instanciar o departamento
				 * 
				 */
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				/*
				 * Ai no caso se existir, vou reaproveitar o
				 * departamento que já existia. 
				 * Se não ele retorna nulo, e vai cair no if abaixo.
				 */
				if(dep == null) {
					dep = instanciandoDepartamento(rs);
					/* guardando no map
					 * O primeiro parametro é
					 * o valor da chave que é o nome do Campo.
					 * e o departamento que é o próprio dep. */
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//Instanciação abaixo está sendo feita dentro do IF
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
