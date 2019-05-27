package gui;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import aplicacao.Main;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoListController implements Initializable, DataChangeListener {
	
	
	//Deped�ncia
	//N�o vou utilizar o new instanciando a classe
	//Pois ai � um acoplamento forte, n�o � de grande utilidade
	//Vou utilizar um SET para puxar as informa��es.
	//E assim vou est� injetando a deped�ncia
	private DepartamentoServico servico;
	
	

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
	
	//Pegar o servico e carregar e mostrar no TableView
	//Vou criar um metodo auxiliar para associar.
	//Esse ObservaBleList faz parte do JavaFX
	private ObservableList<Departamento> obsList;
	
	
	//Criar o metodo de tratamento de eventos do bot�o
	@FXML
	public void onBtNovaAction(ActionEvent evento) {
		//Acessar o Stage pela classe Utils
		Stage parentStage = Utils.palcoAtual(evento);
		//Peguei a referencia para o Stage Atual acima
		//E passo aqui apra criar a minha janela atual
		/*
		 * Como � um bot�o para cadastrar um novo departamento
		 * o formul�rio vai come�ar v�zio
		 * Ent�o eu vou simplemente intstanciar um Departamento v�zio
		 * E n�o vou colocar dado nele
		 * S� que vou injetar ele l� no controllador do Formul�rio.
		 */
		Departamento obj = new Departamento();
		//Vou colocar um parametro a mais no criarDialogForm
		//1� Parametro vai ser o objeto do Departamento
		//2� Parametro vai ser o nome da tela que vou carrear
		//3� Parametro � a Cena da janela atual.
		criarDialogForm(obj,"/gui/DepartamentoForm.fxml", parentStage);
	}
	
	//Criando uma forma de injetar a deped�ncia
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
	
	public void atualizarTabelaView() {
		//Programa��o defensiva
		//Quando o programador n�o utilizar o setDepartamentoServico
		if(servico == null) {
			throw new IllegalStateException("O servico est� nulo");
		}
		//Recuperar os departamentos do SERVICO
		List<Departamento> list = servico.findAll();
		//A partir dessa lista vou carregar dentro do meu ObservableList
		//Instancia o observableList e pega os dados da lista.
		obsList = FXCollections.observableArrayList(list);
		//Carregar os itens na tabelaView e mostrar na tela.
		tabelaViewDepartamento.setItems(obsList);
		
	}
	
	//Metodo auxiliar
	/*
	 * Vou colocar esse metodo recebendo como parametro uma refer�ncia
	 * para o Stage da janela que criou a janela de Dialogo.
	 * carregar janela para preencher um novo departamento
	 * vou ter chamar essa fun��o, l� no bot�o onBtNewAction().
	 */
	private void criarDialogForm(Departamento obj, String nomeAbsoluto, Stage parentStage) {
		//C�digo para instanciar a janela de dialogo.
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			//Chamar o painel carregando a view com a classe Pane que j� existe no JavaFX
			Pane painel = loader.load();
			
			//Injetar o departamento la�no controlador da tela de formul�rio
			//Vou pegar uam refer�ncia para o formul�rio.
			//Com esse objeto abaixo, peguei o controllador da tela que acabei
			//De carrwegar aqui no FXMLLoader que � o formul�rio.
			DepartamentoFormController controller = loader.getController();
			//Injetar nesse controlador o departamento.
			controller.setDepartamento(obj);
			//E puxar o metodo de atualizar 
			//Para carregar os dados do objeto passado no formul�rio.
			/*
			 * Injetando o Departamento Service
			 * depois que criei ele no DepartamentoFormController
			 * Ai tamb�m tenho que utilizar aqui pois se trata da janela.
			 * Injetando manualmente a deped�ncia
			 */
			controller.setDepartamentoServico(new DepartamentoServico());
			//Escutando os eventos com o Observe
			//Do onDataChanged
			/*
			 * E com isso estou me inscrevendo para receber aquele evento
			 * que � onDataChanged
			 * utilizando this, que � a autorefer�ncia.
			 */
			controller.subscribeDataChangeListener(this);
			controller.atualizarDadosFormulario();
			
			
			
			//
			
			/*
			 * Quando eu vou carregar uma janela de dialogo modal na frente janela existente
			 * Eu vou ter que instanciar um novo Stage
			 * ou seja vai ser um palco na frente do outro.
			 */
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite os dados do departamento");
			dialogStage.setScene(new Scene(painel));
			//Esse setResizable diz que a janela pode ou n�o ser redimensionada
			dialogStage.setResizable(false);
			//fun��o que pergunta quem � que � o Stage pai dessa janela ai que entra o parametro 2.
			dialogStage.initOwner(parentStage);
			//Esse metodo vai dizer se a janela � modal ou se tem outro comportamento.
			//E ela vai ser modal sim, vai ficar travada enquanto n�o fechar ela.
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		}catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onDataChanged() {
		//Notifica��o que os dados foram alterados.
		//Vou atualizar os dados da minha tabela
		atualizarTabelaView();
		
	}
	


}
