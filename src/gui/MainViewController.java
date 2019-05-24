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
		//Botando dois parametros, para que n�o tenha que ter dois metodos loadView
		//Na segundo parametro, vou passar uma express�o lambda
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
	 * Criando uma fun��o absoluto, pois vai ser o caminho completo
	 * para o About.
	 * para que n�o seja interrompido, � de grande import�ncia utilizar o
	 * atributo synchronized.
	 * 
	 * A minha fun��o agora vai receber o segundo parametro que vai ser uma express�o lambda
	 * Ent�o botei a interface Consumer e botei do tipo <T>
	 * S� que lembrando que tenho que colocar ela <T> antes do void tamb�m.
	 * Esse tipo <T> � um tipo qualquer.
	 */

	private synchronized <T> void loadView(String nomeAbsoluto, Consumer<T> acaoDeInicializacao) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			// Fazer um objeto do tipo VBOX
			VBox novoVBox = loader.load();
			//Mostrar a view dentro da janela
			//Pegando uma referencia da cena que est� no Main.JAVA
			Scene cenaPrincipal = Main.getCenaPrincipal();
			//Esse metodo root, pega o primeiro elemento da minha VIEW
			//O primeiro elemento � o ScrollPane
			//Vou ter que utilizar um casting
			//E vou ter que puxar o content, que est� entre o Scroll e o VBox
			//E fazer um outro casting para vBox
			//E assim estou fazendo uma referencia para o vBox que est� no MainView
			VBox vBoxPrincipal = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();
			
			//Guardando o 1� filho do vbox no menu
			Node mainMenu = vBoxPrincipal.getChildren().get(0);
			//Limpar todos os outros filhos do meu vBox.
			vBoxPrincipal.getChildren().clear();
			//Adicionar no vBox o mainMenu e depois os filhos do mainVbox
			vBoxPrincipal.getChildren().add(mainMenu);
			//Adicionando a cole��o, que � os filhos do vbox que est� no novoVBox
			vBoxPrincipal.getChildren().addAll(novoVBox.getChildren());
			
			
			//Comando especial para ativar o 2� Parametro que eu passar.
			//Vou criar uma variavel do tipo T
			//Com o nome controller
			//E ela vai receber o meu loader.getController()
			//O meu getController vai retornar o controller do tipo
			//Que eu passar por parametro.
			T controller = loader.getController();
			//Executando a a��o, pegando a variavel que veio pelo parametro
			//E puxando o metodo para executar a fun��o que vier por parametro
			acaoDeInicializacao.accept(controller);
			
			
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
