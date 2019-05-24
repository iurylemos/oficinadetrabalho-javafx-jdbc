package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoListController implements Initializable {
	
	
	//Depedência
	//Não vou utilizar o new instanciando a classe
	//Pois ai é um acoplamento forte, não é de grande utilidade
	//Vou utilizar um SET para puxar as informações.
	//E assim vou está injetando a depedência
	private DepartamentoServico servico;
	
	

	//Criar referências para os nossos componentes da tela
	@FXML
	private TableView<Departamento> tabelaViewDepartamento;
	
	//Criar as colunas
	//1º Parametro: TIPO ENTIDADE que é o DEPARTAMENTO
	//2º Parametro: TIPO COLUNA
	@FXML
	private TableColumn<Departamento, Integer> tabelaColunaId;
	@FXML
	private TableColumn<Departamento, String> tabelaColunaNome;
	
	@FXML
	private Button btNovo;
	
	//Pegar o servico e carregar e mostrar no TableView
	//Vou criar um metodo auxiliar para associar.
	//Esse ObservaBleList faz parte do JavaFX
	private ObservableList<Departamento> obsList;
	
	
	//Criar o metodo de tratamento de eventos do botão
	public void onBtNovaAction() {
		System.out.println("onBtNovaAction");
	}
	
	//Criando uma forma de injetar a depedência
	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//Metodo auxiliar
		incializarNodes();
		
	}





	private void incializarNodes() {
		//Iniciar apropriadamente o comportamento das colunas da tabela.
		//Metodo para adicionar uma celula com valor
		//Vou instanciar a propriedade e botar o nome lá da classe que é id
		tabelaColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		//Botar a tableView para ficar do tamanho da janela.
		/*
		 * Crirei um objeto do tipo Stage
		 * Vou chamar a classe principal, e botar o metodo para puxar a cenaPrincipal
		 * e depois colocar o getWindow = função que pega a referencia para a janela.
		 * Ele Window é de uma SuperClasse do Stage
		 * Então eu tenho que fazer o downcasting para Stage.
		 */
		Stage stage = (Stage) Main.getCenaPrincipal().getWindow();
		//Fazer um comando para a minha tabela de Departamento
		//Ficar do tamanho da janela.
		//Macete para a minha tabela acompanhar o tamanho da janela
		tabelaViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void atualizarTabelaView() {
		//Programação defensiva
		//Quando o programador não utilizar o setDepartamentoServico
		if(servico == null) {
			throw new IllegalStateException("O servico está nulo");
		}
		//Recuperar os departamentos do SERVICO
		List<Departamento> list = servico.findAll();
		//A partir dessa lista vou carregar dentro do meu ObservableList
		//Instancia o observableList e pega os dados da lista.
		obsList = FXCollections.observableArrayList(list);
		//Carregar os itens na tabelaView e mostrar na tela.
		tabelaViewDepartamento.setItems(obsList);
		
	}
	


}
