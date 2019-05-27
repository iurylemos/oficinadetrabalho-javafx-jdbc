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
	@FXML
	public void onBtNovaAction(ActionEvent evento) {
		//Acessar o Stage pela classe Utils
		Stage parentStage = Utils.palcoAtual(evento);
		//Peguei a referencia para o Stage Atual acima
		//E passo aqui apra criar a minha janela atual
		/*
		 * Como é um botão para cadastrar um novo departamento
		 * o formulário vai começar vázio
		 * Então eu vou simplemente intstanciar um Departamento vázio
		 * E não vou colocar dado nele
		 * Só que vou injetar ele lá no controllador do Formulário.
		 */
		Departamento obj = new Departamento();
		//Vou colocar um parametro a mais no criarDialogForm
		//1º Parametro vai ser o objeto do Departamento
		//2º Parametro vai ser o nome da tela que vou carrear
		//3º Parametro é a Cena da janela atual.
		criarDialogForm(obj,"/gui/DepartamentoForm.fxml", parentStage);
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
	
	//Metodo auxiliar
	/*
	 * Vou colocar esse metodo recebendo como parametro uma referência
	 * para o Stage da janela que criou a janela de Dialogo.
	 * carregar janela para preencher um novo departamento
	 * vou ter chamar essa função, lá no botão onBtNewAction().
	 */
	private void criarDialogForm(Departamento obj, String nomeAbsoluto, Stage parentStage) {
		//Código para instanciar a janela de dialogo.
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			//Chamar o painel carregando a view com a classe Pane que já existe no JavaFX
			Pane painel = loader.load();
			
			//Injetar o departamento la´no controlador da tela de formulário
			//Vou pegar uam referência para o formulário.
			//Com esse objeto abaixo, peguei o controllador da tela que acabei
			//De carrwegar aqui no FXMLLoader que é o formulário.
			DepartamentoFormController controller = loader.getController();
			//Injetar nesse controlador o departamento.
			controller.setDepartamento(obj);
			//E puxar o metodo de atualizar 
			//Para carregar os dados do objeto passado no formulário.
			/*
			 * Injetando o Departamento Service
			 * depois que criei ele no DepartamentoFormController
			 * Ai também tenho que utilizar aqui pois se trata da janela.
			 * Injetando manualmente a depedência
			 */
			controller.setDepartamentoServico(new DepartamentoServico());
			//Escutando os eventos com o Observe
			//Do onDataChanged
			/*
			 * E com isso estou me inscrevendo para receber aquele evento
			 * que é onDataChanged
			 * utilizando this, que é a autoreferência.
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
			//Esse setResizable diz que a janela pode ou não ser redimensionada
			dialogStage.setResizable(false);
			//função que pergunta quem é que é o Stage pai dessa janela ai que entra o parametro 2.
			dialogStage.initOwner(parentStage);
			//Esse metodo vai dizer se a janela é modal ou se tem outro comportamento.
			//E ela vai ser modal sim, vai ficar travada enquanto não fechar ela.
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		}catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onDataChanged() {
		//Notificação que os dados foram alterados.
		//Vou atualizar os dados da minha tabela
		atualizarTabelaView();
		
	}
	


}
