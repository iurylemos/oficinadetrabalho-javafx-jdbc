package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidades.Departamento;

public class DepartamentoListController implements Initializable {

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
	
	//Criar o metodo de tratamento de eventos do botão
	
	public void onBtNovaAction() {
		System.out.println("onBtNovaAction");
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
	


}
