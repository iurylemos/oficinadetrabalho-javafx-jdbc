package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
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
		loadView2("/gui/DepartamentoList.fxml");
	}
	
	

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
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
	 */

	private synchronized void loadView(String nomeAbsoluto) {

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
			
			
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String nomeAbsoluto) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			// Fazer um objeto do tipo VBOX
			VBox novoVBox = loader.load();
			//Mostrar a view dentro da janela
			//Pegando uma referencia da cena que está no Main.JAVA
			Scene cenaPrincipal = Main.getCenaPrincipal();
			VBox vBoxPrincipal = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();
			
			//Guardando o 1º filho do vbox no menu
			Node mainMenu = vBoxPrincipal.getChildren().get(0);
			//Limpar todos os outros filhos do meu vBox.
			vBoxPrincipal.getChildren().clear();
			//Adicionar no vBox o mainMenu e depois os filhos do mainVbox
			vBoxPrincipal.getChildren().add(mainMenu);
			//Adicionando a coleção, que é os filhos do vbox que está no novoVBox
			vBoxPrincipal.getChildren().addAll(novoVBox.getChildren());
			
			//loader é o objeto que carrega a view
			//a partir desse objeto eu posso tanto carregar a view
			//quanto também acessar o controler.
			//Ou seja estou pegando uma referencia para o controller dessa VIEW
			//Isso daqui é um processo manual de injetar a depedência lá no controller
			//E depois atualizar os dados na tela do table view
			
			DepartamentoListController controller = loader.getController();
			controller.setDepartamentoServico(new DepartamentoServico());
			controller.atualizarTabelaView();
			
			
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
	}

}
