package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import aplicacao.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.servicos.DepartamentoServico;

public class MainViewController implements Initializable {

	// Itens de controle de tela.
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;

	// Metodos para tratar os eventos do menu.
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		//Botando dois parametros, para que não tenha que ter dois metodos loadView
		//Na segundo parametro, vou passar uma expressão lambda
		loadView("/gui/DepartamentoList.fxml", (DepartamentoListController controller) -> {
			controller.setDepartamentoServico(new DepartamentoServico());
			controller.atualizarTabelaView();
		});
	}
	
	

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x-> {});
	}

	// Metodo da interface initialize
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	/*
	 * Criando uma função absoluto, pois vai ser o caminho completo
	 * para o About.
	 * para que não seja interrompido, é de grande importância utilizar o
	 * atributo synchronized.
	 * 
	 * A minha função agora vai receber o segundo parametro que vai ser uma expressão lambda
	 * Então botei a interface Consumer e botei do tipo <T>
	 * Só que lembrando que tenho que colocar ela <T> antes do void também.
	 * Esse tipo <T> é um tipo qualquer.
	 */

	private synchronized <T> void loadView(String nomeAbsoluto, Consumer<T> acaoDeInicializacao) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			// Fazer um objeto do tipo VBOX
			VBox novoVBox = loader.load();
			//Mostrar a view dentro da janela
			//Pegando uma referencia da cena que está no Main.JAVA
			Scene cenaPrincipal = Main.getCenaPrincipal();
			//Esse metodo root, pega o primeiro elemento da minha VIEW
			//O primeiro elemento é o ScrollPane
			//Vou ter que utilizar um casting
			//E vou ter que puxar o content, que está entre o Scroll e o VBox
			//E fazer um outro casting para vBox
			//E assim estou fazendo uma referencia para o vBox que está no MainView
			VBox vBoxPrincipal = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();
			
			//Guardando o 1º filho do vbox no menu
			Node mainMenu = vBoxPrincipal.getChildren().get(0);
			//Limpar todos os outros filhos do meu vBox.
			vBoxPrincipal.getChildren().clear();
			//Adicionar no vBox o mainMenu e depois os filhos do mainVbox
			vBoxPrincipal.getChildren().add(mainMenu);
			//Adicionando a coleção, que é os filhos do vbox que está no novoVBox
			vBoxPrincipal.getChildren().addAll(novoVBox.getChildren());
			
			
			//Comando especial para ativar o 2º Parametro que eu passar.
			//Vou criar uma variavel do tipo T
			//Com o nome controller
			//E ela vai receber o meu loader.getController()
			//O meu getController vai retornar o controller do tipo
			//Que eu passar por parametro.
			T controller = loader.getController();
			//Executando a ação, pegando a variavel que veio pelo parametro
			//E puxando o metodo para executar a função que vier por parametro
			acaoDeInicializacao.accept(controller);
			
			
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
