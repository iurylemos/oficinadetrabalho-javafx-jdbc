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

	//Criar refer�ncias para os nossos componentes da tela
	@FXML
	private TableView<Departamento> tabelaViewDepartamento;
	
	//Criar as colunas
	//1� Parametro: TIPO ENTIDADE que � o DEPARTAMENTO
	//2� Parametro: TIPO COLUNA
	@FXML
	private TableColumn<Departamento, Integer> tabelaColunaId;
	@FXML
	private TableColumn<Departamento, String> tabelaColunaNome;
	
	@FXML
	private Button btNovo;
	
	//Criar o metodo de tratamento de eventos do bot�o
	
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
		//Vou instanciar a propriedade e botar o nome l� da classe que � id
		tabelaColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		//Botar a tableView para ficar do tamanho da janela.
		/*
		 * Crirei um objeto do tipo Stage
		 * Vou chamar a classe principal, e botar o metodo para puxar a cenaPrincipal
		 * e depois colocar o getWindow = fun��o que pega a referencia para a janela.
		 * Ele Window � de uma SuperClasse do Stage
		 * Ent�o eu tenho que fazer o downcasting para Stage.
		 */
		Stage stage = (Stage) Main.getCenaPrincipal().getWindow();
		//Fazer um comando para a minha tabela de Departamento
		//Ficar do tamanho da janela.
		//Macete para a minha tabela acompanhar o tamanho da janela
		tabelaViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
		
	}
	


}
